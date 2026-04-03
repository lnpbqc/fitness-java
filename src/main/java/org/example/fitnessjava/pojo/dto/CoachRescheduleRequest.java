package org.example.fitnessjava.pojo.dto;

import lombok.Data;

@Data
public class CoachRescheduleRequest {
    private Integer scheduleSlotId;
    private String reason;
}
