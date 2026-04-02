package org.example.fitnessjava.pojo.dto;

import lombok.Data;

@Data
public class BookingUpdateRequest {
    private Integer scheduleSlotId;
    private String location;
}
