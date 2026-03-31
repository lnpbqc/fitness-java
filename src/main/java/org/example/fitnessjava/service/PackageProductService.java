package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.PackageProduct;
import org.example.fitnessjava.pojo.SaleStatus;

import java.util.List;
import java.util.Optional;

public interface PackageProductService {
    List<PackageProduct> getAllPackages();
    Optional<PackageProduct> getPackageById(Long id);
    PackageProduct createPackage(PackageProduct packageProduct);
    Optional<PackageProduct> updatePackage(Long id, PackageProduct packageProduct);
    void deletePackage(Long id);
    Optional<PackageProduct> updateSaleStatus(Long id, SaleStatus saleStatus);
}
