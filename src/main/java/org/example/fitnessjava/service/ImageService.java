package org.example.fitnessjava.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile file, String folder, String uploadIp) throws Exception;

    String buildImageUrl(String folder, String filename);
}
