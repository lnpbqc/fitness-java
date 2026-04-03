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
@Entity(name = "CourseOrder")
@Schema(description = "课程订单")
@EqualsAndHashCode(callSuper = true)
public class CourseOrder extends BaseEntity {
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
