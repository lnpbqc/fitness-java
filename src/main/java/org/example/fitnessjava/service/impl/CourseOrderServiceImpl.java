package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.CourseOrderRepository;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.CourseOrder;
import org.example.fitnessjava.pojo.CourseOrderStatus;
import org.example.fitnessjava.pojo.vo.CourseOrderVO;
import org.example.fitnessjava.service.CourseOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseOrderServiceImpl implements CourseOrderService {

    @Resource
    private CourseOrderRepository courseOrderRepository;

    @Resource
    private ClientRepository clientRepository;

    @Override
    public List<CourseOrderVO> getAllOrders() {
        List<CourseOrder> orders = courseOrderRepository.findAll();
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Optional<CourseOrderVO> getOrderById(Long id) {
        return courseOrderRepository.findById(id).map(this::convertToVO);
    }

    @Override
    public List<CourseOrderVO> getOrdersByStatus(CourseOrderStatus status) {
        List<CourseOrder> orders = courseOrderRepository.findByStatus(status);
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private CourseOrderVO convertToVO(CourseOrder order) {
        CourseOrderVO vo = new CourseOrderVO();
        vo.setId(order.getId());
        vo.setUserId(order.getUserId());
        vo.setPackageId(order.getPackageId());
        vo.setPackageName(order.getPackageName());
        vo.setType(order.getType());
        vo.setTotalSessions(order.getTotalSessions());
        vo.setUsedSessions(order.getUsedSessions());
        vo.setRemainingSessions(order.getRemainingSessions());
        vo.setValidDays(order.getValidDays());
        vo.setStartDate(order.getStartDate());
        vo.setEndDate(order.getEndDate());
        vo.setPurchaseDate(order.getPurchaseDate());
        vo.setPrice(order.getPrice());
        vo.setPointsReward(order.getPointsReward());
        vo.setStatus(order.getStatus());
        
        // 获取用户名
        if (order.getUserId() != null) {
            Optional<Client> clientOpt = clientRepository.findById((long) order.getUserId());
            clientOpt.ifPresent(client -> vo.setUserName(client.getNickname()));
        }
        return vo;
    }

    @Override
    public CourseOrder createOrder(CourseOrder order) {
        if (order.getPrice() == null) {
            order.setPrice(0.0);
        }
        if (order.getTotalSessions() == null) {
            order.setTotalSessions(0);
        }
        if (order.getUsedSessions() == null) {
            order.setUsedSessions(0);
        }
        if (order.getRemainingSessions() == null) {
            order.setRemainingSessions(order.getTotalSessions());
        }
        if (order.getStatus() == null) {
            order.setStatus(CourseOrderStatus.ACTIVE);
        }
        if (order.getPurchaseDate() == null) {
            order.setPurchaseDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        return courseOrderRepository.save(order);
    }

    @Override
    public Optional<CourseOrder> updateOrder(Long id, CourseOrder order) {
        Optional<CourseOrder> optional = courseOrderRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        CourseOrder existing = optional.get();
        if (order.getStatus() != null) {
            existing.setStatus(order.getStatus());
        }
        if (order.getEndDate() != null) {
            existing.setEndDate(order.getEndDate());
        }
        return Optional.of(courseOrderRepository.save(existing));
    }

    @Override
    public Optional<CourseOrder> refundOrder(Long id, String reason) {
        Optional<CourseOrder> optional = courseOrderRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        CourseOrder existing = optional.get();
        existing.setStatus(CourseOrderStatus.REFUNDING);
        return Optional.of(courseOrderRepository.save(existing));
    }

    @Override
    public Optional<CourseOrder> updateSessions(Long id, Integer sessions) {
        Optional<CourseOrder> optional = courseOrderRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        CourseOrder existing = optional.get();
        Integer totalSessions = existing.getTotalSessions() != null ? existing.getTotalSessions() : 0;
        existing.setTotalSessions(totalSessions + sessions);
        existing.setRemainingSessions(existing.getRemainingSessions() + sessions);
        return Optional.of(courseOrderRepository.save(existing));
    }

    @Override
    public void deleteOrder(Long id) {
        courseOrderRepository.deleteById(id);
    }
}
