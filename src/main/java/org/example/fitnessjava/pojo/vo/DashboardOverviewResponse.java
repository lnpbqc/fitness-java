package org.example.fitnessjava.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class DashboardOverviewResponse {
    private Long totalUsers;
    private Long totalCoaches;
    private Long onlineCoaches;
    private Long featuredCoaches;
    private Long totalPackages;
    private Long totalProducts;
    private Long todayBookings;
    private Long pendingBookings;
    private Long confirmedBookings;
    private Long todayPackageOrders;
    private Long todayProductOrders;
    private Long pendingRefunds;
    private Long pendingShipments;
    private List<RevenueData> recentRevenue;
    private List<OrderDistribution> orderDistribution;
    private List<TodoItem> todoItems;

    @Data
    public static class RevenueData {
        private String date;
        private Double amount;
    }

    @Data
    public static class OrderDistribution {
        private String category;
        private Long count;
    }

    @Data
    public static class TodoItem {
        private String id;
        private String title;
        private Long count;
        private Boolean urgent;
    }
}