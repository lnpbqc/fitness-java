package org.example.fitnessjava.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HealthSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private String name;
    private String gender;
    private Integer age;

    private Double height;
    private Double weight;

    private String goal;
    private String frequency;

    private List<String> healthIssues;

    private String notes;
    private String submittedAt;
}