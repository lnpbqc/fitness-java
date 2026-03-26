package org.example.fitnessjava.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CoachScheduleDate")
public class CoachScheduleDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String date;      // e.g., "2023-09-15"
    private String label;     // Human‑readable label, e.g., "Sep 15"
    private String weekday;   // e.g., "Friday"
}
