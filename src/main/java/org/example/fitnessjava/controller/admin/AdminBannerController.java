package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.Banner;
import org.example.fitnessjava.service.BannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/banners")
@Tag(name = "轮播图管理", description = "管理后台轮播图相关接口")
public class AdminBannerController {

    @Resource
    private BannerService bannerService;

    @GetMapping
    @Operation(summary = "获取轮播图列表")
    public ResponseEntity<List<Banner>> getBanners() {
        List<Banner> banners = bannerService.getBanners();
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取轮播图详情")
    public ResponseEntity<Banner> getBanner(@PathVariable Long id) {
        return bannerService.getBannerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "创建轮播图（URL 方式）")
    public ResponseEntity<Banner> createBanner(@RequestBody Banner request) {
        try {
            Banner banner = bannerService.addBanner(request);
            return ResponseEntity.ok(banner);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改轮播图")
    public ResponseEntity<Banner> updateBanner(@PathVariable Long id, @RequestBody Banner request) {
        try {
            Banner banner = bannerService.updateBanner(id, request);
            return ResponseEntity.ok(banner);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除轮播图")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }
}
