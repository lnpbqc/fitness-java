package org.example.fitnessjava.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CourseOrder")
@Schema(description = "课程")
public class CourseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer packageId;

    private String packageName;

    @Enumerated(EnumType.STRING)
    private PackageType type;

    private Integer totalSessions;

    private Integer usedSessions;

    private Integer remainingSessions;

    private Integer validDays;

    private String startDate;

    private String endDate;

    private String purchaseDate;

    private Double price;

    private Integer pointsReward;

    @Enumerated(EnumType.STRING)
    private CourseOrderStatus status;
}
