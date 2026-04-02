package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.Package;
import org.example.fitnessjava.pojo.SaleStatus;
import org.example.fitnessjava.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/packages")
@Tag(name = "套餐管理", description = "管理后台套餐相关接口")
public class AdminPackageController {

    @Resource
    private PackageService packageService;

    @GetMapping
    @Operation(summary = "获取套餐列表")
    public ResponseEntity<List<Package>> getPackages() {
        List<Package> packages = packageService.getAllPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取套餐详情")
    public ResponseEntity<Package> getPackage(@PathVariable Long id) {
        return packageService.getPackageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "创建套餐")
    public ResponseEntity<Package> createPackage(@RequestBody Package request) {
        Package aPackage = packageService.createPackage(request);
        return ResponseEntity.ok(aPackage);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改套餐")
    public ResponseEntity<Package> updatePackage(@PathVariable Long id, @RequestBody Package request) {
        return packageService.updatePackage(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/sale-status")
    @Operation(summary = "上下架套餐")
    public ResponseEntity<Package> updateSaleStatus(@PathVariable Long id, @RequestBody SaleStatus request) {
        return packageService.updateSaleStatus(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除套餐")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.ok().build();
    }
}
