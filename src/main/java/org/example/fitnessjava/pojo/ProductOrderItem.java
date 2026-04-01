package org.example.fitnessjava.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Schema(description = "商品订单子项")
public class ProductOrderItem{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;
    private String name;
    private String specs;
    private Integer quantity;
    private Double price;
    private String image;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;
    // 修改时间
    @UpdateTimestamp
    private LocalDateTime updateTime;
}