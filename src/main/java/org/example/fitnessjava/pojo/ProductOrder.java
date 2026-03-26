package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Integer userId;

    @OneToMany
    private List<ProductOrderItem> items;

    private Double totalAmount;
    private Integer pointsUsed;
    private Double actualPay;

    private String orderDate;

    @Enumerated(EnumType.STRING)
    private ProductOrderStatus status;

    private String statusText;
    private String trackingNumber;
    private String estimatedDelivery;
}