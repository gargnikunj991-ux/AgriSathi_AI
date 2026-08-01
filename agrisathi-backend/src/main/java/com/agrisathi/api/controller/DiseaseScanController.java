package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.DiseaseScanResponse;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.DiseaseScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disease")
@RequiredArgsConstructor
public class DiseaseScanController {

    private final DiseaseScanService diseaseScanService;

    @PostMapping("/scan")
    public ResponseEntity<ApiResponse<DiseaseScanResponse>> scanDisease(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam("image") MultipartFile image) {
        DiseaseScanResponse response = diseaseScanService.scanDisease(currentUser.getId(), image);
        return ResponseEntity.ok(ApiResponse.success("Disease scan completed successfully", response));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<DiseaseScanResponse>>> getScanHistory(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<DiseaseScanResponse> history = diseaseScanService.getScanHistory(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Scan history retrieved successfully", history));
    }
}
