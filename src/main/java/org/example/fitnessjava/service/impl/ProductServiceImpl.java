package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.ProductRepository;
import org.example.fitnessjava.pojo.Product;
import org.example.fitnessjava.pojo.SaleStatus;
import org.example.fitnessjava.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductRepository productRepository;

    @Value("${upload.path}")
    private String uploadBasePath;

    private String getUploadDir() {
        String path = uploadBasePath.endsWith("/") ? uploadBasePath : uploadBasePath + "/";
        return path + "products";
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        if (product.getPrice() == null) {
            product.setPrice(0.0);
        }
        if (product.getStock() == null) {
            product.setStock(0);
        }
        if (product.getSaleStatus() == null) {
            product.setSaleStatus(SaleStatus.ON_SALE);
        }
        if (product.getPointsReward() == null) {
            product.setPointsReward(0);
        }
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product product) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Product existing = optional.get();
        if (product.getName() != null) {
            existing.setName(product.getName());
        }
        if (product.getCategory() != null) {
            existing.setCategory(product.getCategory());
        }
        if (product.getPrice() != null) {
            existing.setPrice(product.getPrice());
        }
        if (product.getImage() != null) {
            existing.setImage(product.getImage());
        }
        if (product.getPointsPrice() != null) {
            existing.setPointsPrice(product.getPointsPrice());
        }
        if (product.getPointsReward() != null) {
            existing.setPointsReward(product.getPointsReward());
        }
        if (product.getDesc() != null) {
            existing.setDesc(product.getDesc());
        }
        if (product.getSaleStatus() != null) {
            existing.setSaleStatus(product.getSaleStatus());
        }
        return Optional.of(productRepository.save(existing));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> updateSaleStatus(Long id, SaleStatus saleStatus) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Product existing = optional.get();
        existing.setSaleStatus(saleStatus);
        return Optional.of(productRepository.save(existing));
    }

    @Override
    public Optional<Product> updateStock(Long id, Integer stock) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Product existing = optional.get();
        existing.setStock(stock);
        return Optional.of(productRepository.save(existing));
    }

    @Override
    public List<String> getCategories() {
        return productRepository.getCategories();
    }

    @Override
    public String uploadProductImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = ".jpg";
        if (originalFilename != null && originalFilename.lastIndexOf('.') > -1) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        String filename = UUID.randomUUID().toString() + fileExtension;

        Path uploadPath = Paths.get(getUploadDir());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/products/" + filename;
    }
}
