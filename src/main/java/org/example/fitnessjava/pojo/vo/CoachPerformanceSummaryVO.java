package org.example.fitnessjava.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class CoachPerformanceSummaryVO {
    private Integer memberCount;
    private Integer monthlyClassHours;
    private Double monthlyCommission;
    private Double courseIncome;
    private List<TrendItem> commissionTrend;
    private List<TrendItem> incomeTrend;
    private List<SalesDataItem> salesData;
    private List<ClassTypeDataItem> classTypeData;
    private List<AchievementItem> achievements;

    @Data
    public static class TrendItem {
        private String date;
        private Double amount;
    }

    @Data
    public static class SalesDataItem {
        private String month;
        private Double amount;
    }

    @Data
    public static class ClassTypeDataItem {
        private String name;
        private Integer value;
        private String color;
    }

    @Data
    public static class AchievementItem {
        private String title;
        private String desc;
        private String color;
        private String icon;
    }
}
