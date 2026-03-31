package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.PackageProduct;
import org.example.fitnessjava.pojo.SaleStatus;
import org.example.fitnessjava.service.PackageProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/packages")
@Tag(name = "套餐管理", description = "管理后台套餐相关接口")
public class AdminPackageController {

    @Resource
    private PackageProductService packageProductService;

    @GetMapping
    @Operation(summary = "获取套餐列表")
    public ResponseEntity<List<PackageProduct>> getPackages() {
        List<PackageProduct> packages = packageProductService.getAllPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取套餐详情")
    public ResponseEntity<PackageProduct> getPackage(@PathVariable Long id) {
        return packageProductService.getPackageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "创建套餐")
    public ResponseEntity<PackageProduct> createPackage(@RequestBody PackageProduct request) {
        PackageProduct packageProduct = packageProductService.createPackage(request);
        return ResponseEntity.ok(packageProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改套餐")
    public ResponseEntity<PackageProduct> updatePackage(@PathVariable Long id, @RequestBody PackageProduct request) {
        return packageProductService.updatePackage(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/sale-status")
    @Operation(summary = "上下架套餐")
    public ResponseEntity<PackageProduct> updateSaleStatus(@PathVariable Long id, @RequestBody SaleStatus request) {
        return packageProductService.updateSaleStatus(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除套餐")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageProductService.deletePackage(id);
        return ResponseEntity.ok().build();
    }
}
