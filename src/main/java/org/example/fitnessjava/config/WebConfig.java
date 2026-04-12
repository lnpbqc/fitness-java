package org.example.fitnessjava.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceLocation = normalizeResourceLocation(uploadPath);
        log.info("Static upload resource location: {}", resourceLocation);

        registry.addResourceHandler("/uploads/**")
            .addResourceLocations(resourceLocation);

        registry.addResourceHandler("/api/uploads/**")
            .addResourceLocations(resourceLocation);
    }

    private String normalizeResourceLocation(String path) {
        if (path == null || path.isBlank()) {
            return "classpath:/uploads/";
        }

        String normalized = path.trim();

        if (normalized.startsWith("classpath:")) {
            String suffix = normalized.substring("classpath:".length());
            if (!suffix.startsWith("/")) {
                suffix = "/" + suffix;
            }
            return ensureTrailingSlash("classpath:" + suffix);
        }

        if (normalized.startsWith("file:")) {
            return ensureTrailingSlash(normalized);
        }

        return ensureTrailingSlash(Paths.get(normalized).toAbsolutePath().normalize().toUri().toString());
    }

    private String ensureTrailingSlash(String value) {
        return value.endsWith("/") ? value : value + "/";
    }
}
