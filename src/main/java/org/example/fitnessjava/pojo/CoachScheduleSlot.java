package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CoachScheduleSlot")
@EqualsAndHashCode(callSuper = true)
public class CoachScheduleSlot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int coachId;
    private String date;           // Expected format: "YYYY-MM-DD"
    private String startTime;      // Expected format: "HH:mm"
    private String endTime;        // Expected format: "HH:mm"
    private boolean available;
    private String roomName;       // Optional – may be null
    private ScheduleType type;
    public enum ScheduleType {
        PRIVATE, //  直接修改available
        TEAM     //  加actual,如果和expected一样再改available
    }
    private Integer expected;       // 开放预约人数
    private Integer actual;         // 实际预约
}
