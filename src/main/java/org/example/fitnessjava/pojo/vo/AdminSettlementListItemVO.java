package org.example.fitnessjava.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminSettlementListItemVO {
    private Long id;
    private Integer bookingId;
    private Integer coachId;
    private String coachName;
    private Integer memberId;
    private String memberName;
    private String settledDate;
    private Double lessonUnitPrice;
    private Double coachRatio;
    private Double coachFee;
    private Double bossFee;
    private Double lessonAmount;
    private LocalDateTime createTime;
}
