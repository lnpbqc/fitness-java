package org.example.fitnessjava.pojo.penddingEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String category;
    private double price;
    private String image;
    private Integer pointsPrice;      // optional
    private Integer pointsReward;     // optional
    @Column(name = "description")
    private String desc;
    private Integer stock;            // optional
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;    // optional
}
