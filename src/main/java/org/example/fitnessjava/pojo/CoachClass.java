package org.example.fitnessjava.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Schema(description = "教练排班")
public class CoachClass{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer coachId;
    private Integer clientId;

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
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;
    // 修改时间
    @UpdateTimestamp
    private LocalDateTime updateTime;
}