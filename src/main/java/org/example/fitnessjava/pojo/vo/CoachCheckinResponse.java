package org.example.fitnessjava.pojo.vo;

import lombok.Data;

@Data
public class CoachCheckinResponse {
    private Integer bookingId;
    private Integer clientId;
    private String clientName;
    private String clientAvatar;
    private String classType;
    private String scheduledTime;
    private Integer remainingSessionsBefore;
    private Integer remainingSessionsAfter;
    private String checkinStatus;
}
