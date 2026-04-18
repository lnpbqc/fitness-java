package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.dao.*;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.vo.CoachCheckinResponse;
import org.example.fitnessjava.service.BookingService;
import org.example.fitnessjava.service.CoachLessonSettlementService;
import org.example.fitnessjava.service.CoachCheckinService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class CoachCheckinServiceImpl implements CoachCheckinService {

    @Resource
    private CheckinTicketRepository checkinTicketRepository;

    @Resource
    private BookingRepository bookingRepository;

    @Resource
    private PackageOrderRepository packageOrderRepository;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private BookingCoachScheduleSlotRepository bookingCoachScheduleSlotRepository;

    @Resource
    private CoachScheduleSlotRepository coachScheduleSlotRepository;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private CoachLessonSettlementService coachLessonSettlementService;

    @Override
    @Transactional
    public CoachCheckinResponse scanAndCheckin(Integer coachId, String qrCode) {
        if (qrCode == null || qrCode.isBlank()) {
            throw new IllegalArgumentException("二维码内容不能为空");
        }

        if (!qrCode.startsWith("MEMBER_QR:")) {
            throw new IllegalArgumentException("无效的二维码格式");
        }
        String content = qrCode.substring(10);
        String[] split = content.split(",");
        Integer memberId = Integer.parseInt(split[0]);
        String memberOpenidFromToken = jwtUtil.getSubjectFromAuthorization(split[1]);
        Client byOpenid = clientRepository.findByOpenid(memberOpenidFromToken);
        if (byOpenid == null) {
            throw new IllegalArgumentException("二维码有误");
        }
        Integer memberIdFromToken = byOpenid.getId();
        if (!memberId.equals(memberIdFromToken)) {
            throw new IllegalArgumentException("二维码有误");
        }
        // 到这里和客户的校验完成了
        // 拿到核销的信息,最近的
        CheckinTicket ticket = checkinTicketRepository.findFirstByMemberIdAndStatusOrderByScheduledTimeAsc(memberId, TicketStatus.UNUSED)
                .orElseThrow(() -> new IllegalArgumentException("无可核销的预约"));

        if (ticket.getStatus() == TicketStatus.USED) {
            throw new IllegalArgumentException("该核销码已被使用");
        }
        if (ticket.getStatus() == TicketStatus.EXPIRED) {
            throw new IllegalArgumentException("该核销码已过期");
        }
        // 这个似乎没什么用
        ticket.setSessionsLeft(-100);
        ticket.setStatus(TicketStatus.USED);
        checkinTicketRepository.save(ticket);

        // 据此找到预约记录
        Booking booking = bookingRepository.findById(ticket.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("关联预约记录不存在"));

        if (booking.getCoachId() != coachId) {
            throw new IllegalArgumentException("无权核销此预约");
        }

        Optional<PackageOrder> packageOrderOptional = packageOrderRepository.findById(Long.valueOf(booking.getPackageOrderId()));
        if (packageOrderOptional.isEmpty()) {
            throw new IllegalArgumentException("套餐id错误");
        }
        PackageOrder packageOrder = packageOrderOptional.get();
        if(packageOrder.getRemainingSessions()<=0){
            throw new IllegalArgumentException("该套餐已用完");
        }

        CoachScheduleSlot slot = coachScheduleSlotRepository.findById(bookingCoachScheduleSlotRepository.findByBookingId(booking.getId()).getCoachScheduleSlotId()).orElse(null);
        if(slot == null){
            throw new IllegalArgumentException("预约时段错误");
        }
        // 预约的时候已经减了
//        LocalDate start = LocalDate.parse(slot.getStartTime());
//        LocalDate end = LocalDate.parse(slot.getEndTime());
//        Integer spentHours = end.atStartOfDay().getHour()-start.atStartOfDay().getHour(); // 一般都是在一天内
//        Integer spentHours = 1;
//        packageOrder.setRemainingSessions(Math.max(0,packageOrder.getRemainingSessions()-spentHours)); // 订单扣减
//        packageOrder.setUsedSessions(packageOrder.getUsedSessions()+spentHours); // 订单扣减
//        packageOrderRepository.save(packageOrder);

        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.setStatusText("已核销");
        bookingRepository.save(booking);
        coachLessonSettlementService.settleForBooking(booking);

        CoachCheckinResponse response = new CoachCheckinResponse();
        response.setBookingId(ticket.getBookingId());
        response.setClientId(ticket.getMemberId());
        response.setClientName(ticket.getMemberName());
        response.setClientAvatar(ticket.getMemberAvatar());
        response.setClassType(ticket.getClassType());
        response.setScheduledTime(ticket.getScheduledTime());
        response.setRemainingSessionsBefore(-1);
        response.setRemainingSessionsAfter(packageOrder.getRemainingSessions());
        response.setCheckinStatus("核销成功");

        return response;
    }
}
