package org.example.fitnessjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/uploads/images")
@Tag(name = "Image Upload", description = "General image upload and access API")
public class ImageController {

    @Resource
    private ImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "images") String folder
    ) {
        try {
            String imageUrl = imageService.uploadImage(file, folder);
            log.info("Upload image successful,url: {}", imageUrl);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "url", imageUrl
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Upload failed: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/url")
    @Operation(summary = "Build image url")
    public ResponseEntity<?> getImageUrl(
            @RequestParam("filename") String filename,
            @RequestParam(value = "folder", required = false, defaultValue = "images") String folder
    ) {
        try {
            return ResponseEntity.ok(Map.of(
                    "url", imageService.buildImageUrl(folder, filename)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }
}
