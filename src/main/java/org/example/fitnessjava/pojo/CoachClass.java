package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CoachClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer coachId;
    private Integer memberId;

    private String memberName;
    private String memberAvatar;
    private String phone;

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

    @Enumerated(EnumType.STRING)
    private CoachClassStatus status;

    private String statusText;
}