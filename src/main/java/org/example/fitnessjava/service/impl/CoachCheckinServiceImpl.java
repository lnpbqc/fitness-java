package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.dao.BookingRepository;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.PackageOrderRepository;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.vo.CoachCheckinResponse;
import org.example.fitnessjava.repository.CheckinTicketRepository;
import org.example.fitnessjava.service.CoachCheckinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class CoachCheckinServiceImpl implements CoachCheckinService {

    @Resource
    private CheckinTicketRepository checkinTicketRepository;

    @Resource
    private BookingRepository bookingRepository;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private CoachRepository coachRepository;

    @Resource
    private PackageOrderRepository packageOrderRepository;

    @Override
    @Transactional
    public CoachCheckinResponse scanAndCheckin(Integer coachId, String qrCode) {
        if (qrCode == null || qrCode.isBlank()) {
            throw new IllegalArgumentException("二维码内容不能为空");
        }

        if (!qrCode.startsWith("MEMBER_QR:")) {
            throw new IllegalArgumentException("无效的二维码格式");
        }

        Integer memberId = Integer.parseInt(qrCode.substring(10));

        CheckinTicket ticket = checkinTicketRepository.findFirstByMemberIdAndStatusOrderByScheduledTimeAsc(memberId, TicketStatus.UNUSED)
                .orElseThrow(() -> new IllegalArgumentException("无可核销的预约"));

        if (ticket.getStatus() == TicketStatus.USED) {
            throw new IllegalArgumentException("该核销码已被使用");
        }
        if (ticket.getStatus() == TicketStatus.EXPIRED) {
            throw new IllegalArgumentException("该核销码已过期");
        }

        Booking booking = bookingRepository.findById(ticket.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("关联预约记录不存在"));

        if (booking.getCoachId() != coachId) {
            throw new IllegalArgumentException("无权核销此预约");
        }

        Integer remainingBefore = ticket.getSessionsLeft() != null ? ticket.getSessionsLeft() : 0;
        int remainingAfter = Math.max(0, remainingBefore - 1);

        ticket.setSessionsLeft(remainingAfter);
        ticket.setStatus(TicketStatus.USED);
        checkinTicketRepository.save(ticket);

        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.setStatusText("已核销");
        bookingRepository.save(booking);

        if (booking.getPackageOrderId() != null && !booking.getPackageOrderId().isBlank()) {
            try {
                Long orderId = Long.valueOf(booking.getPackageOrderId());
                Optional<PackageOrder> orderOpt = packageOrderRepository.findById(orderId);
                if (orderOpt.isPresent()) {
                    PackageOrder order = orderOpt.get();
                    if (order.getType() != PackageType.TIME_CARD) {
                        Integer used = order.getUsedSessions() != null ? order.getUsedSessions() : 0;
                        Integer remaining = order.getRemainingSessions() != null ? order.getRemainingSessions() : 0;
                        order.setUsedSessions(used + 1);
                        order.setRemainingSessions(Math.max(0, remaining));
                        packageOrderRepository.save(order);
                    }
                }
            } catch (NumberFormatException e) {
                log.warn("核销时套餐订单ID格式异常: {}", booking.getPackageOrderId());
            }
        }

        CoachCheckinResponse response = new CoachCheckinResponse();
        response.setBookingId(ticket.getBookingId());
        response.setClientId(ticket.getMemberId());
        response.setClientName(ticket.getMemberName());
        response.setClientAvatar(ticket.getMemberAvatar());
        response.setClassType(ticket.getClassType());
        response.setScheduledTime(ticket.getScheduledTime());
        response.setRemainingSessionsBefore(remainingBefore);
        response.setRemainingSessionsAfter(remainingAfter);
        response.setCheckinStatus("核销成功");

        return response;
    }
}
