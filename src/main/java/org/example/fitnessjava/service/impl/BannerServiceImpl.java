package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.BannerRepository;
import org.example.fitnessjava.pojo.Banner;
import org.example.fitnessjava.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerRepository bannerRepository;

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
}
