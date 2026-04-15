package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.BookingRepository;
import org.example.fitnessjava.dao.CoachLessonSettlementRepository;
import org.example.fitnessjava.dao.CoachWithUserRepository;
import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.BookingStatus;
import org.example.fitnessjava.pojo.CoachLessonSettlement;
import org.example.fitnessjava.pojo.CoachWithUser;
import org.example.fitnessjava.pojo.vo.CoachPerformanceSummaryVO;
import org.example.fitnessjava.service.CoachCompensationService;
import org.example.fitnessjava.service.CoachPerformanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoachPerformanceServiceImpl implements CoachPerformanceService {

    @Resource
    private BookingRepository bookingRepository;

    @Resource
    private CoachWithUserRepository coachWithUserRepository;

    @Resource
    private CoachLessonSettlementRepository coachLessonSettlementRepository;

    @Resource
    private CoachCompensationService coachCompensationService;

    @Override
    public CoachPerformanceSummaryVO getPerformanceSummary(Integer coachId) {
        List<Booking> coachBookings = bookingRepository.findByCoachId(coachId);
        List<CoachLessonSettlement> settlements = coachLessonSettlementRepository.findByCoachId(coachId);

        List<CoachWithUser> relations = coachWithUserRepository.findAllByCoachId(coachId);
        int memberCount = relations.size();

        String currentMonth = LocalDate.now().toString().substring(0, 7);
        long monthlyClassHours = coachBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.CHECKED_IN)
                .filter(b -> b.getBookingDate() != null && b.getBookingDate().startsWith(currentMonth))
                .count();

        double lessonUnitPrice = Optional.ofNullable(coachCompensationService.getSettings().getLessonUnitPrice()).orElse(0.0);
        double monthlyCommission = settlements.stream()
                .filter(s -> s.getSettledDate() != null && s.getSettledDate().startsWith(currentMonth))
                .mapToDouble(s -> s.getCoachFee() == null ? 0 : s.getCoachFee())
                .sum();
        double courseIncome = monthlyClassHours * lessonUnitPrice;

        List<CoachPerformanceSummaryVO.TrendItem> commissionTrend = buildCommissionTrendData(settlements);
        List<CoachPerformanceSummaryVO.TrendItem> incomeTrend = buildIncomeTrendData(coachBookings, lessonUnitPrice);

        List<CoachPerformanceSummaryVO.SalesDataItem> salesData = buildSalesData(coachBookings, lessonUnitPrice);
        List<CoachPerformanceSummaryVO.ClassTypeDataItem> classTypeData = buildClassTypeData(coachBookings);

        List<CoachPerformanceSummaryVO.AchievementItem> achievements = buildAchievements(memberCount, monthlyClassHours, courseIncome);

        CoachPerformanceSummaryVO vo = new CoachPerformanceSummaryVO();
        vo.setMemberCount(memberCount);
        vo.setMonthlyClassHours((int) monthlyClassHours);
        vo.setMonthlyCommission(monthlyCommission);
        vo.setCourseIncome(courseIncome);
        vo.setCommissionTrend(commissionTrend);
        vo.setIncomeTrend(incomeTrend);
        vo.setSalesData(salesData);
        vo.setClassTypeData(classTypeData);
        vo.setAchievements(achievements);

        return vo;
    }

    @Override
    public List<Map<String, Object>> getPerformanceTrend(Integer coachId) {
        List<CoachLessonSettlement> settlements = coachLessonSettlementRepository.findByCoachId(coachId);
        List<Map<String, Object>> trend = new ArrayList<>();

        Map<String, Double> dailyCompleted = settlements.stream()
                .filter(s -> s.getSettledDate() != null)
                .collect(Collectors.groupingBy(
                        CoachLessonSettlement::getSettledDate,
                        Collectors.summingDouble(s -> s.getCoachFee() == null ? 0 : s.getCoachFee())
                ));

        for (Map.Entry<String, Double> entry : dailyCompleted.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList())) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", entry.getKey());
            item.put("amount", entry.getValue());
            trend.add(item);
        }

        return trend;
    }

    private List<CoachPerformanceSummaryVO.TrendItem> buildCommissionTrendData(List<CoachLessonSettlement> settlements) {
        Map<String, Double> monthlyData = settlements.stream()
                .filter(s -> s.getSettledDate() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getSettledDate().substring(0, 7),
                        Collectors.summingDouble(s -> s.getCoachFee() == null ? 0 : s.getCoachFee())
                ));

        return monthlyData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    CoachPerformanceSummaryVO.TrendItem item = new CoachPerformanceSummaryVO.TrendItem();
                    item.setDate(entry.getKey());
                    item.setAmount(entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }

    private List<CoachPerformanceSummaryVO.TrendItem> buildIncomeTrendData(List<Booking> bookings, double lessonUnitPrice) {
        Map<String, Long> monthlyData = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.CHECKED_IN)
                .filter(b -> b.getBookingDate() != null)
                .collect(Collectors.groupingBy(
                        b -> b.getBookingDate().substring(0, 7),
                        Collectors.counting()
                ));

        return monthlyData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    CoachPerformanceSummaryVO.TrendItem item = new CoachPerformanceSummaryVO.TrendItem();
                    item.setDate(entry.getKey());
                    item.setAmount(entry.getValue() * lessonUnitPrice);
                    return item;
                })
                .collect(Collectors.toList());
    }

    private List<CoachPerformanceSummaryVO.SalesDataItem> buildSalesData(List<Booking> bookings, double lessonUnitPrice) {
        Map<String, Long> monthlyData = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.CHECKED_IN)
                .filter(b -> b.getBookingDate() != null)
                .collect(Collectors.groupingBy(
                        b -> b.getBookingDate().substring(0, 7),
                        Collectors.counting()
                ));

        return monthlyData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    CoachPerformanceSummaryVO.SalesDataItem item = new CoachPerformanceSummaryVO.SalesDataItem();
                    item.setMonth(entry.getKey());
                    item.setAmount(entry.getValue() * lessonUnitPrice);
                    return item;
                })
                .collect(Collectors.toList());
    }

    private List<CoachPerformanceSummaryVO.ClassTypeDataItem> buildClassTypeData(List<Booking> bookings) {
        Map<String, Long> typeData = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.CHECKED_IN)
                .filter(b -> b.getLocation() != null)
                .collect(Collectors.groupingBy(Booking::getLocation, Collectors.counting()));

        String[] colors = {"#5470C6", "#91CC75", "#FAC858", "#EE6666", "#73C0DE"};

        List<Map.Entry<String, Long>> sortedEntries = typeData.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        List<CoachPerformanceSummaryVO.ClassTypeDataItem> result = new ArrayList<>();
        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<String, Long> entry = sortedEntries.get(i);
            CoachPerformanceSummaryVO.ClassTypeDataItem item = new CoachPerformanceSummaryVO.ClassTypeDataItem();
            item.setName(entry.getKey());
            item.setValue(entry.getValue().intValue());
            item.setColor(colors[i % colors.length]);
            result.add(item);
        }
        return result;
    }

    private List<CoachPerformanceSummaryVO.AchievementItem> buildAchievements(int memberCount, long classHours, double income) {
        List<CoachPerformanceSummaryVO.AchievementItem> achievements = new ArrayList<>();

        if (memberCount >= 10) {
            CoachPerformanceSummaryVO.AchievementItem item = new CoachPerformanceSummaryVO.AchievementItem();
            item.setTitle("学员突破");
            item.setDesc("绑定学员超过10人");
            item.setColor("#5470C6");
            item.setIcon("users");
            achievements.add(item);
        }

        if (classHours >= 20) {
            CoachPerformanceSummaryVO.AchievementItem item = new CoachPerformanceSummaryVO.AchievementItem();
            item.setTitle("课时达人");
            item.setDesc("本月完成课时超过20节");
            item.setColor("#91CC75");
            item.setIcon("clock");
            achievements.add(item);
        }

        if (income >= 3000) {
            CoachPerformanceSummaryVO.AchievementItem item = new CoachPerformanceSummaryVO.AchievementItem();
            item.setTitle("收入之星");
            item.setDesc("本月课程收入超过3000元");
            item.setColor("#FAC858");
            item.setIcon("star");
            achievements.add(item);
        }

        return achievements;
    }
}
