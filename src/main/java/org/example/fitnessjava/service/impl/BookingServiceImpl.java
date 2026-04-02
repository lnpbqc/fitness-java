package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.dao.BookingRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.CoachScheduleSlotRepository;
import org.example.fitnessjava.dao.CourseOrderRepository;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.CourseOrder;
import org.example.fitnessjava.pojo.CourseOrderStatus;
import org.example.fitnessjava.pojo.PackageType;
import org.example.fitnessjava.pojo.dto.BookingCreateRequest;
import org.example.fitnessjava.pojo.dto.BookingUpdateRequest;
import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.BookingSource;
import org.example.fitnessjava.pojo.BookingStatus;
import org.example.fitnessjava.pojo.CoachScheduleSlot;
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
    private CourseOrderRepository courseOrderRepository;

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

        if (coachId == null) {
            return coachScheduleSlotRepository.findAllByBookingIdInOrderByDateAscStartTimeAsc(bookingIds);
        }
        return coachScheduleSlotRepository.findAllByCoachIdAndBookingIdInOrderByDateAscStartTimeAsc(coachId, bookingIds);
    }

    @Override
    @Transactional
    public Booking createBooking(Integer userId, BookingCreateRequest request) {
        log.info("Create booking request: {}", request);
        validateCreateRequest(request);
        CoachScheduleSlot scheduleSlot = getAvailableScheduleSlot(request.getScheduleSlotId());
        validateUserBookingConflict(userId, scheduleSlot.getDate(), scheduleSlot.getStartTime(), scheduleSlot.getEndTime(), null);

        CourseOrder courseOrder = resolveCourseOrder(userId, request.getPackageOrderId());
        reserveCourseOrderSessions(courseOrder);

        Coach coach = getCoach(scheduleSlot.getCoachId());
        Booking booking = new Booking();
        booking.setUserId(userId);
        fillBookingFromScheduleSlot(booking, scheduleSlot, coach, request.getLocation());
        booking.setStatus(BookingStatus.PENDING);
        booking.setStatusText("待确认");
        booking.setSource(BookingSource.CLIENT);
        booking.setPackageOrderId(String.valueOf(courseOrder.getId()));

        Booking savedBooking = bookingRepository.save(booking);
        occupyScheduleSlot(scheduleSlot, savedBooking.getId());
        log.error("savedBooking: {}", savedBooking);
        return savedBooking;
    }

    @Override
    @Transactional
    public Booking updateBooking(Integer userId, Integer bookingId, BookingUpdateRequest request) {
        Booking booking = getUserBooking(userId, bookingId);
        validateEditableBooking(booking);

        CoachScheduleSlot currentSlot = coachScheduleSlotRepository.findByBookingId(booking.getId()).orElse(null);
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
            occupyScheduleSlot(targetSlot, booking.getId());
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

        coachScheduleSlotRepository.findByBookingId(booking.getId()).ifPresent(this::releaseScheduleSlot);
        restoreCourseOrderSessions(booking.getPackageOrderId());

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
        if (!scheduleSlot.isAvailable() || scheduleSlot.getBookingId() != null) {
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

    private void occupyScheduleSlot(CoachScheduleSlot scheduleSlot, Integer bookingId) {
        scheduleSlot.setAvailable(false);
        scheduleSlot.setBookingId(bookingId);
        coachScheduleSlotRepository.save(scheduleSlot);
    }

    private void releaseScheduleSlot(CoachScheduleSlot scheduleSlot) {
        scheduleSlot.setAvailable(true);
        scheduleSlot.setBookingId(null);
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

    private CourseOrder resolveCourseOrder(Integer userId, String packageOrderId) {
        if (packageOrderId != null && !packageOrderId.isBlank()) {
            Integer orderId = parsePackageOrderId(packageOrderId);
            CourseOrder courseOrder = courseOrderRepository.findById(Long.valueOf(orderId))
                    .orElseThrow(() -> new IllegalArgumentException("课程订单不存在"));
            validateCourseOrder(courseOrder, userId);
            return courseOrder;
        }

        return courseOrderRepository.findByUserId(userId).stream()
                .filter(order -> isCourseOrderAvailable(order, userId))
                .sorted(Comparator.comparing(CourseOrder::getId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("当前用户没有可用的课程订单"));
    }

    private void validateCourseOrder(CourseOrder courseOrder, Integer userId) {
        if (!isCourseOrderAvailable(courseOrder, userId)) {
            throw new IllegalArgumentException("课程订单不可用于预约");
        }
    }

    private boolean isCourseOrderAvailable(CourseOrder courseOrder, Integer userId) {
        if (courseOrder == null || !Objects.equals(courseOrder.getUserId(), userId)) {
            return false;
        }
        if (courseOrder.getStatus() != CourseOrderStatus.ACTIVE) {
            return false;
        }
        if (!isCourseOrderInValidDate(courseOrder)) {
            return false;
        }
        if (courseOrder.getType() == PackageType.TIME_CARD) {
            return true;
        }
        Integer remainingSessions = courseOrder.getRemainingSessions();
        return remainingSessions != null && remainingSessions > 0;
    }

    private boolean isCourseOrderInValidDate(CourseOrder courseOrder) {
        LocalDate today = LocalDate.now();
        if (courseOrder.getStartDate() != null && !courseOrder.getStartDate().isBlank()) {
            LocalDate startDate = LocalDate.parse(courseOrder.getStartDate());
            if (today.isBefore(startDate)) {
                return false;
            }
        }
        if (courseOrder.getEndDate() != null && !courseOrder.getEndDate().isBlank()) {
            LocalDate endDate = LocalDate.parse(courseOrder.getEndDate());
            return !today.isAfter(endDate);
        }
        return true;
    }

    private void reserveCourseOrderSessions(CourseOrder courseOrder) {
        if (courseOrder.getType() == PackageType.TIME_CARD) {
            return;
        }
        Integer remainingSessions = courseOrder.getRemainingSessions();
        if (remainingSessions == null || remainingSessions <= 0) {
            throw new IllegalArgumentException("课程订单剩余课时不足");
        }
        courseOrder.setRemainingSessions(remainingSessions - 1);
        courseOrderRepository.save(courseOrder);
    }

    private void restoreCourseOrderSessions(String packageOrderId) {
        if (packageOrderId == null || packageOrderId.isBlank()) {
            return;
        }
        Integer orderId = parsePackageOrderId(packageOrderId);
        Optional<CourseOrder> courseOrderOptional = courseOrderRepository.findById(Long.valueOf(orderId));
        if (courseOrderOptional.isEmpty()) {
            return;
        }
        CourseOrder courseOrder = courseOrderOptional.get();
        if (courseOrder.getType() == PackageType.TIME_CARD) {
            return;
        }
        Integer remainingSessions = courseOrder.getRemainingSessions() != null ? courseOrder.getRemainingSessions() : 0;
        Integer totalSessions = courseOrder.getTotalSessions() != null ? courseOrder.getTotalSessions() : remainingSessions + 1;
        courseOrder.setRemainingSessions(Math.min(totalSessions, remainingSessions + 1));
        courseOrderRepository.save(courseOrder);
    }

    private Integer parsePackageOrderId(String packageOrderId) {
        try {
            return Integer.valueOf(packageOrderId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("课程订单ID格式不正确");
        }
    }
}
