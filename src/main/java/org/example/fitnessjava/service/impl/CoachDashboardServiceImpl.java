package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.BookingRepository;
import org.example.fitnessjava.dao.NotificationRepository;
import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.BookingStatus;
import org.example.fitnessjava.pojo.NotificationItem;
import org.example.fitnessjava.pojo.vo.CoachDashboardVO;
import org.example.fitnessjava.service.CoachCompensationService;
import org.example.fitnessjava.service.CoachDashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachDashboardServiceImpl implements CoachDashboardService {

    @Resource
    private BookingRepository bookingRepository;

    @Resource
    private NotificationRepository notificationRepository;

    @Resource
    private CoachCompensationService coachCompensationService;

    @Override
    public CoachDashboardVO getDashboard(Integer coachId) {
        String today = LocalDate.now().toString();
        List<Booking> coachBookings = bookingRepository.findByCoachId(coachId);

        long todayBookingCount = coachBookings.stream()
                .filter(b -> today.equals(b.getBookingDate()))
                .count();

        long pendingBookingCount = coachBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.PENDING)
                .count();

        long todayClassCount = coachBookings.stream()
                .filter(b -> today.equals(b.getBookingDate())
                        && (b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING))
                .count();

        long pendingClassCount = coachBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.PENDING)
                .count();

        List<NotificationItem> unreadNotifications = notificationRepository.findByIsRead(false);
        int unreadNotificationCount = unreadNotifications.size();

        CoachDashboardVO vo = new CoachDashboardVO();
        vo.setTodayBookingCount((int) todayBookingCount);
        vo.setPendingBookingCount((int) pendingBookingCount);
        vo.setTodayClassCount((int) todayClassCount);
        vo.setPendingClassCount((int) pendingClassCount);
        double coachFeePerClass = coachCompensationService.estimateCoachFeePerClass();
        vo.setEstimatedIncome(todayClassCount * coachFeePerClass);
        vo.setUnreadNotificationCount(unreadNotificationCount);

        return vo;
    }
}
