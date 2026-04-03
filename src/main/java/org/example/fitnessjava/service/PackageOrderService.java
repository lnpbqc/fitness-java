package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.PackageOrder;
import org.example.fitnessjava.pojo.PackageOrderStatus;
import org.example.fitnessjava.pojo.vo.PackageOrderVO;

import java.util.List;
import java.util.Optional;

public interface PackageOrderService {
    List<PackageOrderVO> getAllOrders();
    Optional<PackageOrderVO> getOrderById(Long id);
    List<PackageOrderVO> getOrdersByStatus(PackageOrderStatus status);
    PackageOrder createOrder(PackageOrder order);
    Optional<PackageOrder> updateOrder(Long id, PackageOrder order);
    Optional<PackageOrder> refundOrder(Long id, String reason);
    Optional<PackageOrder> updateSessions(Long id, Integer sessions);
    void deleteOrder(Long id);
}
