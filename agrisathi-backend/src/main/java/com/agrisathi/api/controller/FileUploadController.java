package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.FileUploadResponse;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileStorageService.uploadFile(currentUser.getId(), file);
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", response));
    }
}
