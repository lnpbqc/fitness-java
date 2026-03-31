package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int coachId;
    private String coachName;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private String location;
    private BookingStatus status;
    private String statusText;
    @Enumerated(EnumType.STRING)
    private BookingSource source;

    // Optional fields (may be null)
    private String coachAvatar;
    private String specialty;
    private String packageOrderId;
    private String phone;
}
