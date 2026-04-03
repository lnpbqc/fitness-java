package org.example.fitnessjava.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingMetricItem {
    private String date;
    private Double weight;
    private Double muscle;
}
