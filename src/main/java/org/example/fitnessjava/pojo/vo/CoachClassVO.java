package org.example.fitnessjava.pojo.vo;

import lombok.Data;
import org.example.fitnessjava.pojo.penddingEntity.CoachClassStatus;

@Data
public class CoachClassVO {
    private Integer id;
    private Integer coachId;
    private String coachName;
    private Integer clientId;
    private String clientName;
    private String clientAvatar;
    private String date;
    private String startTime;
    private String endTime;
    private String type;
    private String roomName;
    private String note;
    private String targetArea;
    private String equipment;
    private Integer sessionNumber;
    private Integer totalSessions;
    private String memberNote;
    private CoachClassStatus status;
    private String statusText;
}
