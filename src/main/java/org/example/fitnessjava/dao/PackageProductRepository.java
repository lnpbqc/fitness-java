package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.penddingEntity.PackageProduct;
import org.example.fitnessjava.pojo.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageProductRepository extends JpaRepository<PackageProduct, Long> {
    List<PackageProduct> findBySaleStatus(SaleStatus saleStatus);
}
