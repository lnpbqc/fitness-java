package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.dao.PackageProductRepository;
import org.example.fitnessjava.pojo.Package;
import org.example.fitnessjava.pojo.SaleStatus;
import org.example.fitnessjava.service.PackageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PackageServiceImpl implements PackageService {

    @Resource
    private PackageProductRepository packageProductRepository;

    @Override
    public List<Package> getAllPackages() {
        return packageProductRepository.findAll();
    }

    @Override
    public Optional<Package> getPackageById(Long id) {
        return packageProductRepository.findById(id);
    }

    @Override
    public Package createPackage(Package aPackage) {
        if (aPackage.getSessions() == null) {
            aPackage.setSessions(0);
        }
        if (aPackage.getValidDays() == null) {
            aPackage.setValidDays(30);
        }
        if (aPackage.getPrice() == null) {
            aPackage.setPrice(0.0);
        }
        if (aPackage.getPointsReward() == null) {
            aPackage.setPointsReward(0);
        }
        if (aPackage.getSaleStatus() == null) {
            aPackage.setSaleStatus(SaleStatus.ON_SALE);
        }
        return packageProductRepository.save(aPackage);
    }

    @Override
    public Optional<Package> updatePackage(Long id, Package aPackage) {
        Optional<Package> optional = packageProductRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Package existing = optional.get();
        if (aPackage.getName() != null) {
            existing.setName(aPackage.getName());
        }
        if (aPackage.getType() != null) {
            existing.setType(aPackage.getType());
        }
        if (aPackage.getSessions() != null) {
            existing.setSessions(aPackage.getSessions());
        }
        if (aPackage.getValidDays() != null) {
            existing.setValidDays(aPackage.getValidDays());
        }
        if (aPackage.getPrice() != null) {
            existing.setPrice(aPackage.getPrice());
        }
        if (aPackage.getPointsReward() != null) {
            existing.setPointsReward(aPackage.getPointsReward());
        }
        if (aPackage.getOriginalPrice() != null) {
            existing.setOriginalPrice(aPackage.getOriginalPrice());
        }
        if (aPackage.getDescription() != null) {
            existing.setDescription(aPackage.getDescription());
        }
        if (aPackage.getSaleStatus() != null) {
            existing.setSaleStatus(aPackage.getSaleStatus());
        }
        return Optional.of(packageProductRepository.save(existing));
    }

    @Override
    public void deletePackage(Long id) {
        packageProductRepository.deleteById(id);
    }

    @Override
    public Optional<Package> updateSaleStatus(Long id, SaleStatus saleStatus) {
        Optional<Package> optional = packageProductRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Package existing = optional.get();
        existing.setSaleStatus(saleStatus);
        return Optional.of(packageProductRepository.save(existing));
    }

    @Override
    public List<String> getAllTypes() {
        return packageProductRepository.findTypes();
    }
}
