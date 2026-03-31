package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.PackageProductRepository;
import org.example.fitnessjava.pojo.PackageProduct;
import org.example.fitnessjava.pojo.SaleStatus;
import org.example.fitnessjava.service.PackageProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackageProductServiceImpl implements PackageProductService {

    @Resource
    private PackageProductRepository packageProductRepository;

    @Override
    public List<PackageProduct> getAllPackages() {
        return packageProductRepository.findAll();
    }

    @Override
    public Optional<PackageProduct> getPackageById(Long id) {
        return packageProductRepository.findById(id);
    }

    @Override
    public PackageProduct createPackage(PackageProduct packageProduct) {
        if (packageProduct.getSessions() == null) {
            packageProduct.setSessions(0);
        }
        if (packageProduct.getValidDays() == null) {
            packageProduct.setValidDays(30);
        }
        if (packageProduct.getPrice() == null) {
            packageProduct.setPrice(0.0);
        }
        if (packageProduct.getPointsReward() == null) {
            packageProduct.setPointsReward(0);
        }
        if (packageProduct.getSaleStatus() == null) {
            packageProduct.setSaleStatus(SaleStatus.ON_SALE);
        }
        return packageProductRepository.save(packageProduct);
    }

    @Override
    public Optional<PackageProduct> updatePackage(Long id, PackageProduct packageProduct) {
        Optional<PackageProduct> optional = packageProductRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        PackageProduct existing = optional.get();
        if (packageProduct.getName() != null) {
            existing.setName(packageProduct.getName());
        }
        if (packageProduct.getType() != null) {
            existing.setType(packageProduct.getType());
        }
        if (packageProduct.getSessions() != null) {
            existing.setSessions(packageProduct.getSessions());
        }
        if (packageProduct.getValidDays() != null) {
            existing.setValidDays(packageProduct.getValidDays());
        }
        if (packageProduct.getPrice() != null) {
            existing.setPrice(packageProduct.getPrice());
        }
        if (packageProduct.getPointsReward() != null) {
            existing.setPointsReward(packageProduct.getPointsReward());
        }
        if (packageProduct.getOriginalPrice() != null) {
            existing.setOriginalPrice(packageProduct.getOriginalPrice());
        }
        if (packageProduct.getDescription() != null) {
            existing.setDescription(packageProduct.getDescription());
        }
        if (packageProduct.getSaleStatus() != null) {
            existing.setSaleStatus(packageProduct.getSaleStatus());
        }
        return Optional.of(packageProductRepository.save(existing));
    }

    @Override
    public void deletePackage(Long id) {
        packageProductRepository.deleteById(id);
    }

    @Override
    public Optional<PackageProduct> updateSaleStatus(Long id, SaleStatus saleStatus) {
        Optional<PackageProduct> optional = packageProductRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        PackageProduct existing = optional.get();
        existing.setSaleStatus(saleStatus);
        return Optional.of(packageProductRepository.save(existing));
    }
}
