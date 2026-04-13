package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "BookingCoachScheduleSlot")
@EqualsAndHashCode(callSuper = true)
public class BookingCoachScheduleSlot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 预约记录ID
    private int bookingId;

    // 关联的排班时段ID
    private int coachScheduleSlotId;

    // ==================  展示层字段（临时/只读） ==================
    @Transient
    private String date;
    @Transient
    private String startTime;
    @Transient
    private String endTime;
    @Transient
    private String location;
    @Transient
    private String coachName;
    @Transient
    private String coachAvatar;
}
