package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.penddingEntity.Product;
import org.example.fitnessjava.pojo.penddingEntity.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySaleStatus(SaleStatus saleStatus);
    List<Product> findByCategory(String category);
}
