package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Package;
import org.example.fitnessjava.pojo.SaleStatus;

import java.util.List;
import java.util.Optional;

public interface PackageService {
    List<Package> getAllPackages();
    Optional<Package> getPackageById(Long id);
    Package createPackage(Package aPackage);
    Optional<Package> updatePackage(Long id, Package aPackage);
    void deletePackage(Long id);
    Optional<Package> updateSaleStatus(Long id, SaleStatus saleStatus);
    List<String> getAllTypes();
}
