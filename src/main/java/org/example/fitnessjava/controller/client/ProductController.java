package org.example.fitnessjava.controller.client;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Product;
import org.example.fitnessjava.service.ProductService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Slf4j
@Tag(name = "客户端商品接口", description = "客户端商品、分类和商品详情查询接口")
public class ProductController {
    @Resource
    private ProductService productService;

    @GetMapping("/all")
    @Operation(summary = "获取商品列表", description = "返回全部商品列表")
    @Cacheable(value = "products",key = "'products'")
    public List<Product> getProducts() {
        List<Product> products = productService.getAllProducts();
        log.info("getProducts:{}", products);
        return products;
    }

    @GetMapping("/categories")
    @Operation(summary = "获取商品分类列表", description = "返回商品分类去重列表")
    @Cacheable(value = "categories")
    public List<String> getCategories() {
        return productService.getCategories();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据商品ID查询商品详情")
    public Product getProduct(
            @Parameter(description = "商品ID", example = "1")
            @PathVariable Long id
    ) {
        return productService.getProductById(id).orElse(null);
    }

}
