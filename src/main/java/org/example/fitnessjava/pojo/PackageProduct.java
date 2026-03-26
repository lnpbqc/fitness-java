package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PackageProduct")
public class PackageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Enumerated(EnumType.STRING)
    private PackageType type;
    private int sessions;
    private int validDays;
    private double price;
    private int pointsReward;

    // Optional fields (nullable)
    private Double originalPrice;       // nullable, corresponds to `originalPrice?`
    private Boolean limitPurchase;      // nullable, corresponds to `limitPurchase?`
    private String description;         // nullable, corresponds to `description?`
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;      // nullable, corresponds to `saleStatus?`
}
