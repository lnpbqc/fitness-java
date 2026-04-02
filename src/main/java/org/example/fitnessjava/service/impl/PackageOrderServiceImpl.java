package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.PackageOrderRepository;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.PackageOrder;
import org.example.fitnessjava.pojo.PackageOrderStatus;
import org.example.fitnessjava.pojo.vo.PackageOrderVO;
import org.example.fitnessjava.service.PackageOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PackageOrderServiceImpl implements PackageOrderService {

    @Resource
    private PackageOrderRepository packageOrderRepository;

    @Resource
    private ClientRepository clientRepository;

    @Override
    public List<PackageOrderVO> getAllOrders() {
        List<PackageOrder> orders = packageOrderRepository.findAll();
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Optional<PackageOrderVO> getOrderById(Long id) {
        return packageOrderRepository.findById(id).map(this::convertToVO);
    }

    @Override
    public List<PackageOrderVO> getOrdersByStatus(PackageOrderStatus status) {
        List<PackageOrder> orders = packageOrderRepository.findByStatus(status);
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private PackageOrderVO convertToVO(PackageOrder order) {
        PackageOrderVO vo = new PackageOrderVO();
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
    public PackageOrder createOrder(PackageOrder order) {
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
            order.setStatus(PackageOrderStatus.ACTIVE);
        }
        if (order.getPurchaseDate() == null) {
            order.setPurchaseDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        return packageOrderRepository.save(order);
    }

    @Override
    public Optional<PackageOrder> updateOrder(Long id, PackageOrder order) {
        Optional<PackageOrder> optional = packageOrderRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        PackageOrder existing = optional.get();
        if (order.getStatus() != null) {
            existing.setStatus(order.getStatus());
        }
        if (order.getEndDate() != null) {
            existing.setEndDate(order.getEndDate());
        }
        return Optional.of(packageOrderRepository.save(existing));
    }

    @Override
    public Optional<PackageOrder> refundOrder(Long id, String reason) {
        Optional<PackageOrder> optional = packageOrderRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        PackageOrder existing = optional.get();
        existing.setStatus(PackageOrderStatus.REFUNDING);
        return Optional.of(packageOrderRepository.save(existing));
    }

    @Override
    public Optional<PackageOrder> updateSessions(Long id, Integer sessions) {
        Optional<PackageOrder> optional = packageOrderRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        PackageOrder existing = optional.get();
        Integer totalSessions = existing.getTotalSessions() != null ? existing.getTotalSessions() : 0;
        existing.setTotalSessions(totalSessions + sessions);
        existing.setRemainingSessions(existing.getRemainingSessions() + sessions);
        return Optional.of(packageOrderRepository.save(existing));
    }

    @Override
    public void deleteOrder(Long id) {
        packageOrderRepository.deleteById(id);
    }
}
