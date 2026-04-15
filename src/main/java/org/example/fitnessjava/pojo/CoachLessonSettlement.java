package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CoachLessonSettlement")
@Table(name = "coach_lesson_settlement",
        uniqueConstraints = {@UniqueConstraint(name = "uk_settlement_booking", columnNames = {"booking_id"})})
@EqualsAndHashCode(callSuper = true)
public class CoachLessonSettlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Integer bookingId;

    @Column(name = "coach_id", nullable = false)
    private Integer coachId;

    @Column(name = "lesson_unit_price", nullable = false)
    private Double lessonUnitPrice;

    @Column(name = "coach_ratio", nullable = false)
    private Double coachRatio;

    @Column(name = "coach_fee", nullable = false)
    private Double coachFee;

    @Column(name = "boss_fee", nullable = false)
    private Double bossFee;

    @Column(name = "settled_date", nullable = false, length = 10)
    private String settledDate;
}
