package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.dao.*;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.dto.BookingCreateRequest;
import org.example.fitnessjava.pojo.dto.BookingUpdateRequest;
import org.example.fitnessjava.pojo.vo.BookingVO;
import org.example.fitnessjava.dao.CheckinTicketRepository;
import org.example.fitnessjava.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Resource
    private CoachScheduleSlotRepository coachScheduleSlotRepository;

    @Resource
    private BookingRepository bookingRepository;

    @Resource
    private CoachRepository coachRepository;

    @Resource
    private PackageOrderRepository packageOrderRepository;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private CheckinTicketRepository checkinTicketRepository;

    @Resource
    private BookingCoachScheduleSlotRepository bookingCoachScheduleSlotRepository;

    // ==================== 管理后台接口实现 ====================

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookingVO> getAllBookingsWithUserInfo() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(this::convertToBookingVO).toList();
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public List<Booking> getBookingsByCoachId(int coachId) {
        return bookingRepository.findByCoachId(coachId);
    }

    @Override
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<BookingVO> getBookingsWithUserInfo(BookingStatus status, Integer coachId, Integer userId) {
        List<Booking> bookings = bookingRepository.findBookings(status, coachId, userId);
        return bookings.stream().map(this::convertToBookingVO).toList();
    }

    @Override
    public Optional<Booking> getBookingById(Integer id) {
        return bookingRepository.findById(id);
    }

    @Override
    public BookingVO getBookingByIdWithUserInfo(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预约记录不存在"));
        return convertToBookingVO(booking);
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBookingStatus(Integer id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预约记录不存在"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking cancelBooking(Integer id, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预约记录不存在"));
        int coachScheduleSlotId = bookingCoachScheduleSlotRepository.findByBookingId(booking.getId()).getCoachScheduleSlotId();
        coachScheduleSlotRepository.findById(coachScheduleSlotId).ifPresent(this::releaseScheduleSlot);
        restorePackageOrderSessions(booking.getPackageOrderId());
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setStatusText("已取消" + (reason != null ? "：" + reason : ""));
        return bookingRepository.save(booking);
    }

    @Override
    public Booking confirmBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预约记录不存在"));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setStatusText("已确认");
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Integer id) {
        bookingRepository.deleteById(id);
    }

    private BookingVO convertToBookingVO(Booking booking) {
        BookingVO vo = new BookingVO();
        vo.setId(booking.getId());
        vo.setUserId(booking.getUserId());
        vo.setCoachId(booking.getCoachId());
        vo.setBookingDate(booking.getBookingDate());
        vo.setStartTime(booking.getStartTime());
        vo.setEndTime(booking.getEndTime());
        vo.setLocation(booking.getLocation());
        vo.setStatus(booking.getStatus());
        vo.setStatusText(booking.getStatusText());
        vo.setSource(booking.getSource());
        vo.setPackageOrderId(booking.getPackageOrderId());

        // 查询用户名
        clientRepository.findById((long) booking.getUserId())
                .ifPresent(client -> vo.setUserName(client.getNickname()));

        // 查询教练信息
        coachRepository.findById((long) booking.getCoachId()).ifPresent(coach -> {
            vo.setCoachName(coach.getNickname());
            vo.setCoachAvatar(coach.getAvatar());
            vo.setSpecialty(coach.getSpecialty());
            vo.setPhone(coach.getPhone());
        });

        return vo;
    }

    // ==================== 客户端接口实现 ====================

    @Override
    public List<CoachScheduleSlot> getScheduleSlots(Integer coachId, Integer clientId) {
        if (clientId == null) {
            if (coachId == null) {
                return coachScheduleSlotRepository.findAllByOrderByDateAscStartTimeAsc();
            }
            return coachScheduleSlotRepository.findAllByCoachIdOrderByDateAscStartTimeAsc(coachId);
        }

        List<Booking> bookings = bookingRepository.findAllByUserId(clientId);
        if (bookings.isEmpty()) {
            return new ArrayList<>();
        }

        // 客户维度查询需要先通过预约记录拿到排班关联的 bookingId。
        Set<Integer> bookingIds = new LinkedHashSet<>();
        for (Booking booking : bookings) {
            bookingIds.add(booking.getId());
        }
        ArrayList<CoachScheduleSlot> coachScheduleSlots = new ArrayList<>();
        for(Integer bookingId : bookingIds) {
            BookingCoachScheduleSlot byBookingId = bookingCoachScheduleSlotRepository.findByBookingId(bookingId);
            if (byBookingId != null) {
                coachScheduleSlotRepository.findById(byBookingId.getCoachScheduleSlotId()).ifPresent(coachScheduleSlot -> {
                    coachScheduleSlots.add(coachScheduleSlot);
                });
            }
        }

        return coachScheduleSlots;
    }

    @Override
    public List<BookingCoachScheduleSlot> getBookingsForClient(int userId) {
        List<Booking> bookings = bookingRepository.findAllByUserId(userId);
        List<BookingCoachScheduleSlot> result = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingCoachScheduleSlot mapping = bookingCoachScheduleSlotRepository.findByBookingId(booking.getId());
            if (mapping != null) {
                // 尝试填充展示字段
                CoachScheduleSlot slot = coachScheduleSlotRepository.findById(mapping.getCoachScheduleSlotId()).orElse(null);
                if (slot != null) {
                    mapping.setDate(slot.getDate());
                    mapping.setStartTime(slot.getStartTime());
                    mapping.setEndTime(slot.getEndTime());
                    mapping.setLocation(slot.getRoomName());
                    // 教练信息
                    coachRepository.findById((long) slot.getCoachId()).ifPresent(coach -> {
                        mapping.setCoachId(coach.getId());
                        mapping.setCoachName(coach.getNickname());
                        mapping.setCoachAvatar(coach.getAvatar());
                    });
                }
                result.add(mapping);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public Booking createBooking(Integer userId, BookingCreateRequest request) {
        log.info("Create booking request: {}", request);
        validateCreateRequest(request);
        CoachScheduleSlot scheduleSlot = getAvailableScheduleSlot(request.getScheduleSlotId());
        validateUserBookingConflict(userId, scheduleSlot.getDate(), scheduleSlot.getStartTime(), scheduleSlot.getEndTime(), null);

        PackageOrder packageOrder = resolvePackageOrder(userId, request.getPackageOrderId());
        reservePackageOrderSessions(packageOrder);

        Coach coach = getCoach(scheduleSlot.getCoachId());
        Booking booking = new Booking();
        booking.setUserId(userId);
        fillBookingFromScheduleSlot(booking, scheduleSlot, coach, request.getLocation());
        booking.setStatus(BookingStatus.PENDING);
        booking.setStatusText("待确认");
        booking.setSource(BookingSource.CLIENT);
        booking.setPackageOrderId(String.valueOf(packageOrder.getId()));

        Booking savedBooking = bookingRepository.save(booking);
        occupyScheduleSlot(scheduleSlot);

        createCheckinTicket(savedBooking, packageOrder);

        BookingCoachScheduleSlot bookingCoachScheduleSlot = new BookingCoachScheduleSlot();
        bookingCoachScheduleSlot.setBookingId(booking.getId());
        bookingCoachScheduleSlot.setCoachScheduleSlotId(scheduleSlot.getId());
        bookingCoachScheduleSlotRepository.save(bookingCoachScheduleSlot);

        log.info("Booking created: id={}, userId={}, coachId={}",
                savedBooking.getId(), savedBooking.getUserId(), savedBooking.getCoachId());
        return savedBooking;
    }

    private void createCheckinTicket(Booking booking, PackageOrder packageOrder) {
        Client client = clientRepository.findById((long) booking.getUserId()).orElse(null);
        if (client == null) {
            return;
        }

        CheckinTicket ticket = new CheckinTicket();
        ticket.setBookingId(booking.getId());
        ticket.setQrCode("MEMBER_QR:" + client.getId());
        ticket.setMemberId(client.getId());
        ticket.setMemberName(client.getNickname() != null ? client.getNickname() : "");
        ticket.setMemberAvatar(client.getAvatar() != null ? client.getAvatar() : "");
        ticket.setClassType(booking.getBookingDate());
        ticket.setScheduledTime(booking.getBookingDate() + " " + booking.getStartTime());
        ticket.setSessionsLeft(packageOrder.getRemainingSessions());
        ticket.setStatus(TicketStatus.UNUSED);
        checkinTicketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Booking updateBooking(Integer userId, Integer bookingId, BookingUpdateRequest request) {
        Booking booking = getUserBooking(userId, bookingId);
        validateEditableBooking(booking);

        int coachScheduleSlotId = bookingCoachScheduleSlotRepository.findByBookingId(bookingId).getCoachScheduleSlotId();
        CoachScheduleSlot currentSlot = coachScheduleSlotRepository.findById(coachScheduleSlotId).orElse(null);
        CoachScheduleSlot targetSlot = currentSlot;
        if (request != null && request.getScheduleSlotId() != null) {
            if (currentSlot == null || currentSlot.getId() != request.getScheduleSlotId()) {
                targetSlot = getAvailableScheduleSlot(request.getScheduleSlotId());
                validateUserBookingConflict(userId, targetSlot.getDate(), targetSlot.getStartTime(), targetSlot.getEndTime(), booking.getId());
            }
        }

        if (targetSlot == null) {
            throw new IllegalArgumentException("预约缺少排班时段");
        }

        if (currentSlot != null && currentSlot.getId() != targetSlot.getId()) {
            releaseScheduleSlot(currentSlot);
            occupyScheduleSlot(targetSlot);
        }

        boolean slotChanged = currentSlot == null || currentSlot.getId() != targetSlot.getId();
        String requestLocation = request != null ? request.getLocation() : null;
        if (slotChanged) {
            Coach coach = getCoach(targetSlot.getCoachId());
            fillBookingFromScheduleSlot(booking, targetSlot, coach, requestLocation);
        } else if (requestLocation != null && !requestLocation.isBlank()) {
            booking.setLocation(requestLocation);
        }
        booking.setStatusText("待确认");
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking cancelBooking(Integer userId, Integer bookingId) {
        Booking booking = getUserBooking(userId, bookingId);
        validateCancelableBooking(booking);

        int coachScheduleSlotId = bookingCoachScheduleSlotRepository.findByBookingId(bookingId).getCoachScheduleSlotId();
        coachScheduleSlotRepository.findById(coachScheduleSlotId).ifPresent(this::releaseScheduleSlot);
        restorePackageOrderSessions(booking.getPackageOrderId());

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setStatusText("已取消");
        return bookingRepository.save(booking);
    }

    private void validateCreateRequest(BookingCreateRequest request) {
        if (request == null || request.getScheduleSlotId() == null) {
            throw new IllegalArgumentException("预约时必须传入排班时段ID");
        }
    }

    private CoachScheduleSlot getAvailableScheduleSlot(Integer scheduleSlotId) {
        CoachScheduleSlot scheduleSlot = coachScheduleSlotRepository.findById(scheduleSlotId)
                .orElseThrow(() -> new IllegalArgumentException("排班时段不存在"));
        if (!scheduleSlot.isAvailable()) {
            throw new IllegalArgumentException("当前排班时段已被预约");
        }
        return scheduleSlot;
    }

    private Coach getCoach(Integer coachId) {
        return coachRepository.findById(Long.valueOf(coachId))
                .orElseThrow(() -> new IllegalArgumentException("教练不存在"));
    }

    private Booking getUserBooking(Integer userId, Integer bookingId) {
        return bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new IllegalArgumentException("预约记录不存在"));
    }

    private void validateEditableBooking(Booking booking) {
        if (booking.getStatus() == BookingStatus.CANCELLED
                || booking.getStatus() == BookingStatus.COMPLETED
                || booking.getStatus() == BookingStatus.CHECKED_IN) {
            throw new IllegalArgumentException("当前预约状态不支持修改");
        }
    }

    private void validateCancelableBooking(Booking booking) {
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("预约已取消，请勿重复操作");
        }
        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CHECKED_IN) {
            throw new IllegalArgumentException("当前预约状态不支持取消");
        }
    }

    private void fillBookingFromScheduleSlot(Booking booking, CoachScheduleSlot scheduleSlot, Coach coach, String location) {
        booking.setCoachId(scheduleSlot.getCoachId());
        booking.setBookingDate(scheduleSlot.getDate());
        booking.setStartTime(scheduleSlot.getStartTime());
        booking.setEndTime(scheduleSlot.getEndTime());
        booking.setLocation(location != null && !location.isBlank() ? location : scheduleSlot.getRoomName());
    }

    private void occupyScheduleSlot(CoachScheduleSlot scheduleSlot) {
        log.info("schedule id:{}", scheduleSlot.getId());
        if(scheduleSlot.isAvailable()){
            if(scheduleSlot.getType()== CoachScheduleSlot.ScheduleType.PRIVATE){
                scheduleSlot.setAvailable(false);
            }else {
                scheduleSlot.setActual(scheduleSlot.getActual()+1);
                if(scheduleSlot.getExpected()==scheduleSlot.getActual()){
                    scheduleSlot.setAvailable(false);
                }
            }
        }
        coachScheduleSlotRepository.save(scheduleSlot);
    }

    private void releaseScheduleSlot(CoachScheduleSlot scheduleSlot) {
        if(scheduleSlot.getType()== CoachScheduleSlot.ScheduleType.TEAM){
            scheduleSlot.setActual(scheduleSlot.getActual()-1);
        }
        scheduleSlot.setAvailable(true);
        coachScheduleSlotRepository.save(scheduleSlot);
    }

    private void validateUserBookingConflict(Integer userId, String date, String startTime, String endTime, Integer excludeBookingId) {
        List<Booking> bookings = bookingRepository.findAllByUserId(userId);
        LocalTime newStartTime = LocalTime.parse(startTime);
        LocalTime newEndTime = LocalTime.parse(endTime);
        for (Booking booking : bookings) {
            if (excludeBookingId != null && booking.getId() == excludeBookingId) {
                continue;
            }
            if (booking.getStatus() == BookingStatus.CANCELLED
                    || booking.getStatus() == BookingStatus.COMPLETED
                    || booking.getStatus() == BookingStatus.CHECKED_IN) {
                continue;
            }
            if (!Objects.equals(booking.getBookingDate(), date)) {
                continue;
            }
            LocalTime existingStart = LocalTime.parse(booking.getStartTime());
            LocalTime existingEnd = LocalTime.parse(booking.getEndTime());
            boolean overlap = newStartTime.isBefore(existingEnd) && newEndTime.isAfter(existingStart);
            if (overlap) {
                throw new IllegalArgumentException("当前时间段已存在预约冲突");
            }
        }
    }

    private PackageOrder resolvePackageOrder(Integer userId, String packageOrderId) {
        if (packageOrderId != null && !packageOrderId.isBlank()) {
            Integer orderId = parsePackageOrderId(packageOrderId);
            PackageOrder packageOrder = packageOrderRepository.findById(Long.valueOf(orderId))
                    .orElseThrow(() -> new IllegalArgumentException("套餐订单不存在"));
            validatePackageOrder(packageOrder, userId);
            return packageOrder;
        }

        return packageOrderRepository.findByUserId(userId).stream()
                .filter(order -> isPackageOrderAvailable(order, userId))
                .sorted(Comparator.comparing(PackageOrder::getId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("当前用户没有可用的套餐订单"));
    }

    private void validatePackageOrder(PackageOrder packageOrder, Integer userId) {
        if (!isPackageOrderAvailable(packageOrder, userId)) {
            throw new IllegalArgumentException("套餐订单不可用于预约");
        }
    }

    private boolean isPackageOrderAvailable(PackageOrder packageOrder, Integer userId) {
        if (packageOrder == null || !Objects.equals(packageOrder.getUserId(), userId)) {
            return false;
        }
        if (packageOrder.getStatus() != PackageOrderStatus.ACTIVE) {
            return false;
        }
        if (!isPackageOrderInValidDate(packageOrder)) {
            return false;
        }
        if (packageOrder.getType() == PackageType.TIME_CARD) {
            return true;
        }
        Integer remainingSessions = packageOrder.getRemainingSessions();
        return remainingSessions != null && remainingSessions > 0;
    }

    private boolean isPackageOrderInValidDate(PackageOrder packageOrder) {
        LocalDate today = LocalDate.now();
        if (packageOrder.getStartDate() != null && !packageOrder.getStartDate().isBlank()) {
            LocalDate startDate = LocalDate.parse(packageOrder.getStartDate());
            if (today.isBefore(startDate)) {
                return false;
            }
        }
        if (packageOrder.getEndDate() != null && !packageOrder.getEndDate().isBlank()) {
            LocalDate endDate = LocalDate.parse(packageOrder.getEndDate());
            return !today.isAfter(endDate);
        }
        return true;
    }

    private void reservePackageOrderSessions(PackageOrder packageOrder) {
        if (packageOrder.getType() == PackageType.TIME_CARD) {
            return;
        }
        Integer remainingSessions = packageOrder.getRemainingSessions();
        if (remainingSessions == null || remainingSessions <= 0) {
            throw new IllegalArgumentException("套餐订单剩余课时不足");
        }
        packageOrder.setRemainingSessions(remainingSessions - 1);
        packageOrderRepository.save(packageOrder);
    }

    private void restorePackageOrderSessions(String packageOrderId) {
        if (packageOrderId == null || packageOrderId.isBlank()) {
            return;
        }
        Integer orderId = parsePackageOrderId(packageOrderId);
        Optional<PackageOrder> packageOrderOptional = packageOrderRepository.findById(Long.valueOf(orderId));
        if (packageOrderOptional.isEmpty()) {
            return;
        }
        PackageOrder packageOrder = packageOrderOptional.get();
        if (packageOrder.getType() == PackageType.TIME_CARD) {
            return;
        }
        Integer remainingSessions = packageOrder.getRemainingSessions() != null ? packageOrder.getRemainingSessions() : 0;
        Integer totalSessions = packageOrder.getTotalSessions() != null ? packageOrder.getTotalSessions() : remainingSessions + 1;
        packageOrder.setRemainingSessions(Math.min(totalSessions, remainingSessions + 1));
        packageOrderRepository.save(packageOrder);
    }

    private Integer parsePackageOrderId(String packageOrderId) {
        try {
            return Integer.valueOf(packageOrderId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("套餐订单ID格式不正确");
        }
    }
}
