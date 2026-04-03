package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BodyAssessmentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private Integer coachId;

    private String date;

    private Double height;
    private Double weight;
    private Double bodyFat;
    private Double muscleMass;
    private Double bmi;
    private Double visceralFat;

    private Double chest;
    private Double waist;
    private Double hips;

    private Double leftArm;
    private Double rightArm;

    private Double leftThigh;
    private Double rightThigh;

    private Double leftCalf;
    private Double rightCalf;
    private List<String> photos;

    private String notes;
}