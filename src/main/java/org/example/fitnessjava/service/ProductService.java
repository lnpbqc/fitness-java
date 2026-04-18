package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Product;
import org.example.fitnessjava.pojo.SaleStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    Optional<Product> updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Optional<Product> updateSaleStatus(Long id, SaleStatus saleStatus);
    Optional<Product> updateStock(Long id, Integer stock);
    List<String> getCategories();
    String uploadProductImage(MultipartFile file) throws Exception;
}
