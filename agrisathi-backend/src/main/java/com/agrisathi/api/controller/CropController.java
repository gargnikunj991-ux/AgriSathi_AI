package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.CropRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.CropResponse;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.CropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Crop APIs", description = "Endpoints for tracking active crops, planting schedules, and harvest history")
@SecurityRequirement(name = "bearerAuth")
public class CropController {

    private final CropService cropService;

    @GetMapping
    @Operation(summary = "Get All Crops", description = "Retrieves a list of all crops registered under the authenticated farmer's account.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of crops retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<ApiResponse<List<CropResponse>>> getAllCrops(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<CropResponse> crops = cropService.getCropsByUserId(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Crops retrieved successfully", crops));
    }

    @GetMapping("/{cropId}")
    @Operation(summary = "Get Crop by ID", description = "Retrieves details of a specific crop by its unique ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Crop details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Crop not found or does not belong to user")
    })
    public ResponseEntity<ApiResponse<CropResponse>> getCropById(
            @Parameter(description = "ID of the crop to retrieve", example = "1") @PathVariable Long cropId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        CropResponse crop = cropService.getCropByIdAndUserId(cropId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Crop details retrieved successfully", crop));
    }

    @PostMapping
    @Operation(summary = "Add New Crop", description = "Registers a new crop with sowing date, expected harvest date, and status.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Crop added successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error in request payload")
    })
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
    @Operation(summary = "Update Crop Details", description = "Updates details or growth status of an existing crop.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Crop updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Crop not found")
    })
    public ResponseEntity<ApiResponse<CropResponse>> updateCrop(
            @Parameter(description = "ID of the crop to update", example = "1") @PathVariable Long cropId,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody CropRequest request) {
        CropResponse updatedCrop = cropService.updateCrop(cropId, currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Crop updated successfully", updatedCrop));
    }

    @DeleteMapping("/{cropId}")
    @Operation(summary = "Delete Crop", description = "Removes a crop entry from the farmer's record.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Crop deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Crop not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteCrop(
            @Parameter(description = "ID of the crop to delete", example = "1") @PathVariable Long cropId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        cropService.deleteCrop(cropId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Crop deleted successfully"));
    }
}
