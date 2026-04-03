package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.CourseOrder;
import org.example.fitnessjava.pojo.CourseOrderStatus;
import org.example.fitnessjava.pojo.vo.CourseOrderVO;

import java.util.List;
import java.util.Optional;

public interface CourseOrderService {
    List<CourseOrderVO> getAllOrders();
    Optional<CourseOrderVO> getOrderById(Long id);
    List<CourseOrderVO> getOrdersByUserId(Integer userId);
    List<CourseOrderVO> getOrdersByStatus(CourseOrderStatus status);
    CourseOrder createOrder(CourseOrder order);
    Optional<CourseOrder> updateOrder(Long id, CourseOrder order);
    Optional<CourseOrder> refundOrder(Long id, String reason);
    Optional<CourseOrder> updateSessions(Long id, Integer sessions);
    void deleteOrder(Long id);

    List<CourseOrderVO> getOrdersOfMine(String openid);
}
