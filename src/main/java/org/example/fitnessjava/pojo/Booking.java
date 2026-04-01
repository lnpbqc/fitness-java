package org.example.fitnessjava.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Booking")
@Schema(description = "预约")
@EqualsAndHashCode(callSuper = true)
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int coachId;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private String location;
    private BookingStatus status;
    private String statusText;
    @Enumerated(EnumType.STRING)
    private BookingSource source;
    private String packageOrderId;
}
