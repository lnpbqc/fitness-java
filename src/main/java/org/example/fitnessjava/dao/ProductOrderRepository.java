package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.ProductOrder;
import org.example.fitnessjava.pojo.ProductOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    List<ProductOrder> findByStatus(ProductOrderStatus status);
    List<ProductOrder> findByUserId(Integer userId);
}
