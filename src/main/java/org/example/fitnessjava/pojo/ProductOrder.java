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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Schema(description = "商品订单")
@EqualsAndHashCode(callSuper = true)
public class ProductOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<ProductOrderItem> items;

    private Double totalAmount;
    private Integer pointsUsed;
    private Integer pointsReward;
    private Double actualPay;

    private String orderDate;

    @Enumerated(EnumType.STRING)
    private ProductOrderStatus status;

    private String statusText;
    private String trackingNumber;
    private String estimatedDelivery;
}