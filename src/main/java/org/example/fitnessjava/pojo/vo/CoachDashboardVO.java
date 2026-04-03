package org.example.fitnessjava.pojo.vo;

import lombok.Data;

@Data
public class CoachDashboardVO {
    private Integer todayBookingCount;
    private Integer pendingBookingCount;
    private Integer todayClassCount;
    private Integer pendingClassCount;
    private Double estimatedIncome;
    private Integer unreadNotificationCount;
}
