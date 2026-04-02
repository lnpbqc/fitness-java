package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.PackageOrder;
import org.example.fitnessjava.pojo.PackageOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageOrderRepository extends JpaRepository<PackageOrder, Long> {
    List<PackageOrder> findByStatus(PackageOrderStatus status);
    List<PackageOrder> findByUserId(Integer userId);
    List<PackageOrder> findByPurchaseDate(String purchaseDate);
}
