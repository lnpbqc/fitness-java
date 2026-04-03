package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.ProductOrderRepository;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.ProductOrder;
import org.example.fitnessjava.pojo.ProductOrderStatus;
import org.example.fitnessjava.pojo.vo.ProductOrderVO;
import org.example.fitnessjava.service.ProductOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Resource
    private ProductOrderRepository productOrderRepository;

    @Resource
    private ClientRepository clientRepository;

    @Override
    public List<ProductOrderVO> getAllOrders() {
        List<ProductOrder> orders = productOrderRepository.findAll();
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductOrderVO> getOrderById(Long id) {
        return productOrderRepository.findById(id).map(this::convertToVO);
    }

    @Override
    public List<ProductOrderVO> getOrdersByStatus(ProductOrderStatus status) {
        List<ProductOrder> orders = productOrderRepository.findByStatus(status);
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<ProductOrderVO> getOrdersByUserId(Integer userId) {
        List<ProductOrder> orders = productOrderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private ProductOrderVO convertToVO(ProductOrder order) {
        ProductOrderVO vo = new ProductOrderVO();
        vo.setId(order.getId());
        vo.setUserId(order.getUserId());
        vo.setItems(order.getItems());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPointsUsed(order.getPointsUsed());
        vo.setPointsReward(order.getPointsReward());
        vo.setActualPay(order.getActualPay());
        vo.setOrderDate(order.getOrderDate());
        vo.setStatus(order.getStatus());
        vo.setStatusText(order.getStatusText());
        vo.setTrackingNumber(order.getTrackingNumber());
        vo.setEstimatedDelivery(order.getEstimatedDelivery());
        
        if (order.getUserId() != null) {
            Optional<Client> clientOpt = clientRepository.findById((long) order.getUserId());
            clientOpt.ifPresent(client -> vo.setUserName(client.getNickname()));
        }
        
        if (order.getItems() != null) {
            vo.setItemCount(order.getItems().size());
        }
        
        return vo;
    }

    @Override
    public ProductOrder createOrder(ProductOrder order) {
        if (order.getTotalAmount() == null) {
            order.setTotalAmount(0.0);
        }
        if (order.getActualPay() == null) {
            order.setActualPay(0.0);
        }
        if (order.getPointsUsed() == null) {
            order.setPointsUsed(0);
        }
        if (order.getStatus() == null) {
            order.setStatus(ProductOrderStatus.PENDING);
        }
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        return productOrderRepository.save(order);
    }

    @Override
    public Optional<ProductOrder> updateOrder(Long id, ProductOrder order) {
        Optional<ProductOrder> optional = productOrderRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        ProductOrder existing = optional.get();
        if (order.getStatus() != null) {
            existing.setStatus(order.getStatus());
        }
        if (order.getStatusText() != null) {
            existing.setStatusText(order.getStatusText());
        }
        if (order.getEstimatedDelivery() != null) {
            existing.setEstimatedDelivery(order.getEstimatedDelivery());
        }
        return Optional.of(productOrderRepository.save(existing));
    }

    @Override
    public Optional<ProductOrder> refundOrder(Long id, String reason) {
        Optional<ProductOrder> optional = productOrderRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        ProductOrder existing = optional.get();
        existing.setStatus(ProductOrderStatus.CANCELLED);
        existing.setStatusText("退款：" + reason);
        return Optional.of(productOrderRepository.save(existing));
    }

    @Override
    public Optional<ProductOrder> shipOrder(Long id, String trackingNumber) {
        Optional<ProductOrder> optional = productOrderRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        ProductOrder existing = optional.get();
        existing.setStatus(ProductOrderStatus.SHIPPED);
        existing.setTrackingNumber(trackingNumber);
        existing.setStatusText("已发货");
        return Optional.of(productOrderRepository.save(existing));
    }

    @Override
    public void deleteOrder(Long id) {
        productOrderRepository.deleteById(id);
    }
}
