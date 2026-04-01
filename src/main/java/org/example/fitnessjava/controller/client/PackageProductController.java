package org.example.fitnessjava.controller.client;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.Package;
import org.example.fitnessjava.service.PackageProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client/package")
@Tag(name = "套餐信息", description = "管理套餐相关接口")
public class PackageProductController {

    @Resource
    private PackageProductService packageProductService;

    @GetMapping("/all")

    @Operation(description = "获取所有套餐")
    public List<Package> getAllPackageProducts() {
        return packageProductService.getAllPackages();
    }

    @GetMapping("/types")
    @Operation(description = "获取所有类型")
    public List<String> getAllPackageProductTypes() {
        return packageProductService.getAllTypes();
    }

}
