package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.CropRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.CropResponse;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.CropService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crops")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CropResponse>>> getAllCrops(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<CropResponse> crops = cropService.getCropsByUserId(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Crops retrieved successfully", crops));
    }

    @GetMapping("/{cropId}")
    public ResponseEntity<ApiResponse<CropResponse>> getCropById(
            @PathVariable Long cropId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        CropResponse crop = cropService.getCropByIdAndUserId(cropId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Crop details retrieved successfully", crop));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CropResponse>> addCrop(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody CropRequest request) {
        CropResponse createdCrop = cropService.addCrop(currentUser.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Crop added successfully", createdCrop),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{cropId}")
    public ResponseEntity<ApiResponse<CropResponse>> updateCrop(
            @PathVariable Long cropId,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody CropRequest request) {
        CropResponse updatedCrop = cropService.updateCrop(cropId, currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Crop updated successfully", updatedCrop));
    }

    @DeleteMapping("/{cropId}")
    public ResponseEntity<ApiResponse<Void>> deleteCrop(
            @PathVariable Long cropId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        cropService.deleteCrop(cropId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Crop deleted successfully"));
    }
}
