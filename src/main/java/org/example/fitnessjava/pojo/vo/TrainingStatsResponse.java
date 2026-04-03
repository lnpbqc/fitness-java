package org.example.fitnessjava.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fitnessjava.pojo.dto.TrainingMetricItem;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingStatsResponse {
    private Integer monthTrainingCount;
    private Double currentWeight;
    private List<TrainingMetricItem> metrics;
}
