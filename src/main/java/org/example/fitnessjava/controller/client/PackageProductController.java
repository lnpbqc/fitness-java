package org.example.fitnessjava.controller.client;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.fitnessjava.pojo.PackageProduct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client/package")
@Tag(name = "套餐信息", description = "管理套餐相关接口")
public class PackageProductController {

    @GetMapping("/all")
    public List<PackageProduct> getAllPackageProducts() {
        return null;
    }

    @GetMapping("/types")
    public List<String> getAllPackageProductTypes() {
        return null;
    }

    @GetMapping("/{type}")
    public List<PackageProduct> getPackageProductByType(@PathVariable String type) {
        return null;
    }
}
