package com.agrisathi.api.service;

import com.agrisathi.api.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    FileUploadResponse uploadFile(Long userId, MultipartFile file);
    FileUploadResponse uploadImage(Long userId, MultipartFile image);
    List<FileUploadResponse> getUserUploadedFiles(Long userId);
}

