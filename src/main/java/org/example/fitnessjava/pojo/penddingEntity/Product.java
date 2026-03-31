package org.example.fitnessjava.pojo.penddingEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fitnessjava.pojo.SaleStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String category;
    private Double price;
    private String image;
    private Integer pointsPrice;
    private Integer pointsReward;
    @Column(name = "description")
    private String desc;
    private Integer stock;
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;
}
