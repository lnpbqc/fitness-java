package org.example.fitnessjava.pojo.vo;

import lombok.Data;
import org.example.fitnessjava.pojo.TicketStatus;

@Data
public class CheckinTicketVO {
    private Integer bookingId;
    private String qrCode;
    private Integer memberId;
    private String memberName;
    private String memberAvatar;
    private String classType;
    private String scheduledTime;
    private Integer sessionsLeft;
    private TicketStatus status;
    private String statusText;
}
