package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.dao.BookingRepository;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.CoachScheduleSlotRepository;
import org.example.fitnessjava.dao.PackageOrderRepository;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.dto.CoachProxyBookingRequest;
import org.example.fitnessjava.pojo.dto.CoachRescheduleRequest;
import org.example.fitnessjava.pojo.vo.BookingVO;
import org.example.fitnessjava.service.BookingService;
import org.example.fitnessjava.service.CoachBookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CoachBookingServiceImpl implements CoachBookingService {

    @Resource
    private BookingRepository bookingRepository;

    @Resource
    private CoachScheduleSlotRepository coachScheduleSlotRepository;

    @Resource
    private CoachRepository coachRepository;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private PackageOrderRepository packageOrderRepository;

    @Resource
    private BookingService bookingService;

    @Override
    public List<BookingVO> getBookingsByCoachId(Integer coachId, String date) {
        List<Booking> bookings = bookingRepository.findByCoachId(coachId);
        if (date != null && !date.isEmpty()) {
            bookings = bookings.stream()
                    .filter(b -> Objects.equals(b.getBookingDate(), date))
                    .collect(Collectors.toList());
        }
        return bookings.stream().map(this::convertToBookingVO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Booking rescheduleBooking(Integer coachId, Integer bookingId, CoachRescheduleRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("预约记录不存在"));

        if (booking.getCoachId() != coachId) {
            throw new IllegalArgumentException("无权操作此预约");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED
                || booking.getStatus() == BookingStatus.COMPLETED
                || booking.getStatus() == BookingStatus.CHECKED_IN) {
            throw new IllegalArgumentException("当前预约状态不支持改期");
        }

        CoachScheduleSlot currentSlot = coachScheduleSlotRepository.findByBookingId(booking.getId())
                .orElseThrow(() -> new IllegalArgumentException("预约缺少排班时段"));

        if (request.getScheduleSlotId() == null) {
            throw new IllegalArgumentException("请指定新的排班时段");
        }

        if (currentSlot.getId() == request.getScheduleSlotId()) {
            throw new IllegalArgumentException("与原时段相同");
        }

        CoachScheduleSlot targetSlot = coachScheduleSlotRepository.findById(request.getScheduleSlotId())
                .orElseThrow(() -> new IllegalArgumentException("目标排班时段不存在"));

        if (!targetSlot.isAvailable() || targetSlot.getBookingId() != null) {
            throw new IllegalArgumentException("目标时段已被占用");
        }

        currentSlot.setAvailable(true);
        currentSlot.setBookingId(null);
        coachScheduleSlotRepository.save(currentSlot);

        booking.setCoachId(targetSlot.getCoachId());
        booking.setBookingDate(targetSlot.getDate());
        booking.setStartTime(targetSlot.getStartTime());
        booking.setEndTime(targetSlot.getEndTime());
        booking.setStatus(BookingStatus.PENDING);
        booking.setStatusText("待确认" + (request.getReason() != null ? "（教练改期：" + request.getReason() + "）" : ""));
        Booking savedBooking = bookingRepository.save(booking);

        targetSlot.setAvailable(false);
        targetSlot.setBookingId(savedBooking.getId());
        coachScheduleSlotRepository.save(targetSlot);

        return savedBooking;
    }

    @Override
    @Transactional
    public Booking proxyBooking(Integer coachId, CoachProxyBookingRequest request) {
        if (request.getClientId() == null) {
            throw new IllegalArgumentException("请指定会员");
        }
        if (request.getScheduleSlotId() == null) {
            throw new IllegalArgumentException("请指定排班时段");
        }

        Client client = clientRepository.findById(Long.valueOf(request.getClientId()))
                .orElseThrow(() -> new IllegalArgumentException("会员不存在"));

        CoachScheduleSlot scheduleSlot = coachScheduleSlotRepository.findById(request.getScheduleSlotId())
                .orElseThrow(() -> new IllegalArgumentException("排班时段不存在"));

        if (!scheduleSlot.isAvailable() || scheduleSlot.getBookingId() != null) {
            throw new IllegalArgumentException("当前排班时段已被预约");
        }

        validateUserBookingConflict(client.getId(), scheduleSlot.getDate(),
                scheduleSlot.getStartTime(), scheduleSlot.getEndTime(), null);

        PackageOrder packageOrder = resolvePackageOrder(client.getId(), request.getPackageOrderId());
        reservePackageOrderSessions(packageOrder);

        Coach coach = coachRepository.findById(Long.valueOf(coachId))
                .orElseThrow(() -> new IllegalArgumentException("教练不存在"));

        Booking booking = new Booking();
        booking.setUserId(client.getId());
        booking.setCoachId(scheduleSlot.getCoachId());
        booking.setBookingDate(scheduleSlot.getDate());
        booking.setStartTime(scheduleSlot.getStartTime());
        booking.setEndTime(scheduleSlot.getEndTime());
        booking.setLocation(request.getLocation() != null && !request.getLocation().isBlank()
                ? request.getLocation() : scheduleSlot.getRoomName());
        booking.setStatus(BookingStatus.PENDING);
        booking.setStatusText("待确认（教练代约）");
        booking.setSource(BookingSource.COACH_PROXY);
        booking.setPackageOrderId(packageOrder != null ? String.valueOf(packageOrder.getId()) : null);

        Booking savedBooking = bookingRepository.save(booking);

        scheduleSlot.setAvailable(false);
        scheduleSlot.setBookingId(savedBooking.getId());
        coachScheduleSlotRepository.save(scheduleSlot);

        return savedBooking;
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

        clientRepository.findById((long) booking.getUserId())
                .ifPresent(client -> {
                    vo.setUserName(client.getNickname());
                    vo.setPhone(client.getPhone());
                });

        coachRepository.findById((long) booking.getCoachId()).ifPresent(coach -> {
            vo.setCoachName(coach.getNickname());
            vo.setCoachAvatar(coach.getAvatar());
            vo.setSpecialty(coach.getSpecialty());
        });

        return vo;
    }

    private void validateUserBookingConflict(Integer userId, String date, String startTime, String endTime, Integer excludeBookingId) {
        List<Booking> bookings = bookingRepository.findAllByUserId(userId);
        java.time.LocalTime newStartTime = java.time.LocalTime.parse(startTime);
        java.time.LocalTime newEndTime = java.time.LocalTime.parse(endTime);
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
            java.time.LocalTime existingStart = java.time.LocalTime.parse(booking.getStartTime());
            java.time.LocalTime existingEnd = java.time.LocalTime.parse(booking.getEndTime());
            boolean overlap = newStartTime.isBefore(existingEnd) && newEndTime.isAfter(existingStart);
            if (overlap) {
                throw new IllegalArgumentException("当前时间段已存在预约冲突");
            }
        }
    }

    private PackageOrder resolvePackageOrder(Integer userId, String packageOrderId) {
        if (packageOrderId != null && !packageOrderId.isBlank()) {
            Integer orderId = Integer.valueOf(packageOrderId);
            PackageOrder packageOrder = packageOrderRepository.findById(Long.valueOf(orderId))
                    .orElseThrow(() -> new IllegalArgumentException("套餐订单不存在"));
            if (!Objects.equals(packageOrder.getUserId(), userId)) {
                throw new IllegalArgumentException("套餐订单不属于该会员");
            }
            if (packageOrder.getStatus() != PackageOrderStatus.ACTIVE) {
                throw new IllegalArgumentException("套餐订单状态异常");
            }
            return packageOrder;
        }

        return packageOrderRepository.findByUserId(userId).stream()
                .filter(order -> {
                    if (order.getStatus() != PackageOrderStatus.ACTIVE) return false;
                    if (order.getType() == PackageType.TIME_CARD) return true;
                    Integer remaining = order.getRemainingSessions();
                    return remaining != null && remaining > 0;
                })
                .sorted(java.util.Comparator.comparing(PackageOrder::getId))
                .findFirst()
                .orElse(null);
    }

    private void reservePackageOrderSessions(PackageOrder packageOrder) {
        if (packageOrder == null) return;
        if (packageOrder.getType() == PackageType.TIME_CARD) return;
        Integer remainingSessions = packageOrder.getRemainingSessions();
        if (remainingSessions == null || remainingSessions <= 0) {
            throw new IllegalArgumentException("套餐订单剩余课时不足");
        }
        packageOrder.setRemainingSessions(remainingSessions - 1);
        packageOrderRepository.save(packageOrder);
    }
}
