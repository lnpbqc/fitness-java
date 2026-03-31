package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.ProductOrder;
import org.example.fitnessjava.pojo.ProductOrderStatus;
import org.example.fitnessjava.pojo.vo.ProductOrderVO;

import java.util.List;
import java.util.Optional;

public interface ProductOrderService {
    List<ProductOrderVO> getAllOrders();
    Optional<ProductOrderVO> getOrderById(Long id);
    List<ProductOrderVO> getOrdersByStatus(ProductOrderStatus status);
    ProductOrder createOrder(ProductOrder order);
    Optional<ProductOrder> updateOrder(Long id, ProductOrder order);
    Optional<ProductOrder> refundOrder(Long id, String reason);
    Optional<ProductOrder> shipOrder(Long id, String trackingNumber);
    void deleteOrder(Long id);
}
