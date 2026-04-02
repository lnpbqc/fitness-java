package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.BannerRepository;
import org.example.fitnessjava.pojo.Banner;
import org.example.fitnessjava.service.BannerService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerRepository bannerRepository;

    private static final String UPLOAD_DIR = "D:/ForMyself/fitness-admin-all/uploads/banners";

    @Override
    public ArrayList<Banner> getBanners() {
        return new ArrayList<>(bannerRepository.findAll());
    }

    @Override
    public Optional<Banner> getBannerById(Long id) {
        return bannerRepository.findById(id);
    }

    @Override
    public Banner addBanner(Banner banner) {
        return bannerRepository.save(banner);
    }

    @Override
    public Banner updateBanner(Long id, Banner banner) {
        Banner existingBanner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("轮播图不存在"));
        
        if (banner.getImage() != null) {
            existingBanner.setImage(banner.getImage());
        }
        if (banner.getLinkType() != null) {
            existingBanner.setLinkType(banner.getLinkType());
        }
        if (banner.getLinkValue() != null) {
            existingBanner.setLinkValue(banner.getLinkValue());
        }
        
        return bannerRepository.save(existingBanner);
    }

    @Override
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    @Override
    public Banner uploadBanner(MultipartFile file, String linkType, String linkValue) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? 
                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        
        String filename = java.util.UUID.randomUUID().toString() + fileExtension;

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = "/api/uploads/banners/" + filename;

        Banner banner = new Banner();
        banner.setImage(imageUrl);
        banner.setLinkType(linkType != null ? linkType : "");
        banner.setLinkValue(linkValue != null ? linkValue : "");

        return bannerRepository.save(banner);
    }
}
