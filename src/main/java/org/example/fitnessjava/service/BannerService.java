package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Banner;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

public interface BannerService {
    ArrayList<Banner> getBanners();
    Optional<Banner> getBannerById(Long id);
    Banner addBanner(Banner banner);
    Banner updateBanner(Long id, Banner banner);
    void deleteBanner(Long id);
    Banner uploadBanner(MultipartFile file, String linkType, String linkValue) throws Exception;
}
