package org.example.fitnessjava.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保路径以 / 结尾
        String absolutePath = uploadPath;
        if (!absolutePath.endsWith("/")) {
            absolutePath = absolutePath + "/";
        }
        
        // Spring 需要 file: 前缀，后跟绝对路径
        String fileUrl = "file:" + absolutePath;
        System.out.println("配置静态资源路径：" + fileUrl);
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(fileUrl);
        
        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations(fileUrl);
    }
}
