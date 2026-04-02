package org.example.fitnessjava.pojo.dto;

import lombok.Data;

@Data
public class BookingCreateRequest {
    private Integer scheduleSlotId;
    private String location;
    private String packageOrderId;
}
