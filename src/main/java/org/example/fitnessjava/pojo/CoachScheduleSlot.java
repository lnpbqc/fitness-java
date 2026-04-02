package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CoachScheduleSlot")
public class CoachScheduleSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int coachId;
    private String date;           // Expected format: "YYYY-MM-DD"
    private String startTime;      // Expected format: "HH:mm"
    private String endTime;        // Expected format: "HH:mm"
    private boolean available;
    private String roomName;       // Optional – may be null
    private Integer bookingId;     // Optional – may be null
}
