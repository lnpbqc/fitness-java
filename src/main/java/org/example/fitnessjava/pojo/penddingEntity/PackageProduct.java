package org.example.fitnessjava.pojo.penddingEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fitnessjava.pojo.PackageType;
import org.example.fitnessjava.pojo.SaleStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PackageProduct")
public class PackageProduct {
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
