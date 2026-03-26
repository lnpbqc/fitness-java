package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CourseOrder")
public class CourseOrder {
    // Unique identifier of the order
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    // Identifier of the user who placed the order
    private int userId;

    // Identifier of the package purchased
    private int packageId;

    // Human‑readable name of the package
    private String packageName;

    // Type of the package
    @Enumerated(EnumType.STRING)
    private PackageType type;

    // Total number of sessions included in the package
    private int totalSessions;

    // Number of sessions already used
    private int usedSessions;

    // Number of sessions still available
    private int remainingSessions;

    // Number of days the order remains valid
    private int validDays;

    // Date when the order becomes effective (ISO‑8601 string)
    private String startDate;

    // Date when the order expires (ISO‑8601 string)
    private String endDate;

    // Date when the order was purchased (ISO‑8601 string)
    private String purchaseDate;

    // Price paid for the order
    private double price;

    // Reward points earned from the purchase
    private int pointsReward;

    // Current status of the order
    @Enumerated(EnumType.STRING)
    private CourseOrderStatus status;
}
