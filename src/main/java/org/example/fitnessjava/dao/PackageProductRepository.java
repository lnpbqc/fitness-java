package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.Package;
import org.example.fitnessjava.pojo.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageProductRepository extends JpaRepository<Package, Long> {
    List<Package> findBySaleStatus(SaleStatus saleStatus);
    @Query("SELECT DISTINCT pp.type FROM Package pp")
    List<String> findTypes();
}
