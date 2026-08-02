package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.FileUploadResponse;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        return new ResponseEntity<>(ApiResponse.success("Image uploaded successfully", response), HttpStatus.CREATED);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadImage(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam("image") MultipartFile image) {
        FileUploadResponse response = fileStorageService.uploadImage(currentUser.getId(), image);
        return new ResponseEntity<>(ApiResponse.success("Image uploaded successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FileUploadResponse>>> getUserFiles(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<FileUploadResponse> files = fileStorageService.getUserUploadedFiles(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("User files retrieved successfully", files));
    }
}
