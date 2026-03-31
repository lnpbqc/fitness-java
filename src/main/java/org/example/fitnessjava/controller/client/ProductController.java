package org.example.fitnessjava.controller.client;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
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
public class ProductController {
    @Resource
    private ProductService productService;

    @GetMapping("/all")
    @Cacheable(value = "products",key = "'products'")
    public List<Product> getProducts() {
        List<Product> products = productService.getAllProducts();
        log.info("getProducts:{}", products);
        return products;
    }

    @GetMapping("/categories")
    @Cacheable(value = "categories")
    public List<String> getCategories() {
        return productService.getCategories();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id).orElse(null);
    }

}
