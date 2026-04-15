package org.example.fitnessjava.pojo.vo;

import lombok.Data;

@Data
public class AdminSettlementSummaryVO {
    private Long totalCount;
    private Double totalLessonAmount;
    private Double totalCoachFee;
    private Double totalBossFee;
}
