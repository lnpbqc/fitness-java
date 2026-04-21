package org.example.fitnessjava.service.impl;

import org.example.fitnessjava.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".webp", ".gif", ".bmp", ".svg"
    );

    @Value("${upload.path}")
    private String uploadBasePath;

    @Override
    public String uploadImage(MultipartFile file, String folder) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file cannot be empty");
        }

        String safeFolder = normalizeFolder(folder);
        String extension = resolveExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Unsupported image type: " + extension);
        }

        String filename = UUID.randomUUID() + extension.toLowerCase(Locale.ROOT);
        Path uploadDir = Paths.get(resolveUploadDir(safeFolder));
        ensureDirectory(uploadDir);

        Path targetPath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return buildImageUrl(safeFolder, filename);
    }

    @Override
    public String buildImageUrl(String folder, String filename) {
        String safeFolder = normalizeFolder(folder);
        return "/uploads/" + safeFolder + "/" + filename;
    }

    private String resolveUploadDir(String folder) {
        String base = uploadBasePath.endsWith("/") ? uploadBasePath : uploadBasePath + "/";
        return base + folder;
    }

    private void ensureDirectory(Path uploadDir) throws IOException {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    private String normalizeFolder(String folder) {
        if (folder == null || folder.isBlank()) {
            return "images";
        }
        String normalized = folder.trim().replace("\\", "/");
        if (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        if (!normalized.matches("[a-zA-Z0-9/_-]+")) {
            throw new IllegalArgumentException("Folder contains invalid characters");
        }
        if (normalized.contains("..")) {
            throw new IllegalArgumentException("Folder cannot contain '..'");
        }
        return normalized.isBlank() ? "images" : normalized;
    }

    private String resolveExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return ".jpg";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }
}
