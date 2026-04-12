package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CheckinTicket {

    @Id
    private Integer bookingId;
    private String qrCode;
    private Integer memberId;
    private String memberName;
    private String memberAvatar;
    private String classType;
    private String scheduledTime;
    private Integer sessionsLeft;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}
