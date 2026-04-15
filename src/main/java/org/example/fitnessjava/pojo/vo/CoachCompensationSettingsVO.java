package org.example.fitnessjava.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CoachCompensationSettingsVO {
    private Double lessonUnitPrice;
    private Double coachRatio;
    private Double coachFeePerClass;
    private Double bossFeePerClass;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
