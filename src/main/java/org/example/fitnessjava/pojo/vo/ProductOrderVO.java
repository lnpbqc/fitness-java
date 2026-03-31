package org.example.fitnessjava.pojo.vo;

import lombok.Data;
import org.example.fitnessjava.pojo.ProductOrderItem;
import org.example.fitnessjava.pojo.ProductOrderStatus;

import java.util.List;

@Data
public class ProductOrderVO {
    private Integer id;
    private Integer userId;
    private String userName;
    private List<ProductOrderItem> items;
    private Integer itemCount;
    private Double totalAmount;
    private Integer pointsUsed;
    private Double actualPay;
    private String orderDate;
    private ProductOrderStatus status;
    private String statusText;
    private String trackingNumber;
    private String estimatedDelivery;
}
