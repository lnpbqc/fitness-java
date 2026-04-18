package org.example.fitnessjava.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientOrderRequest {

    private String type; // "PACKAGE" or "SHOP"

    private Integer pointsUsed;

    // PACKAGE order fields
    private Integer packageId;

    // SHOP order fields
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Integer productId;
        private Integer quantity;
    }
}
