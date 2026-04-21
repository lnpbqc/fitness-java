package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Banner;

import java.util.ArrayList;
import java.util.Optional;

public interface BannerService {
    ArrayList<Banner> getBanners();
    Optional<Banner> getBannerById(Long id);
    Banner addBanner(Banner banner);
    Banner updateBanner(Long id, Banner banner);
    void deleteBanner(Long id);
}
