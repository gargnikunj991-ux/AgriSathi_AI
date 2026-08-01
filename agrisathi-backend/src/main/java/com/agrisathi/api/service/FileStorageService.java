package com.agrisathi.api.service;

import com.agrisathi.api.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileUploadResponse uploadFile(Long userId, MultipartFile file);
}
