package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.Banner;
import org.example.fitnessjava.service.BannerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传轮播图（文件上传）")
    public ResponseEntity<?> uploadBanner(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "linkType", required = false) String linkType,
            @RequestParam(value = "linkValue", required = false) String linkValue) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "上传文件不能为空"
                ));
            }

            Banner banner = bannerService.uploadBanner(file, linkType, linkValue);
            return ResponseEntity.ok(banner);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "上传失败：" + e.getMessage()
            ));
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
