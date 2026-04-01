package org.example.fitnessjava.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Package")
@Schema(description = "套餐")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    private PackageType type;
    private Integer sessions;
    private Integer validDays;
    private Double price;
    private Integer pointsReward;

    // Optional fields (nullable)
    private Double originalPrice;
    private Boolean limitPurchase;
    private String description;
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;
}
