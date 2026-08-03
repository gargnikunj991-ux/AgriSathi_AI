package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.DiseaseScanResponse;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.DiseaseScanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disease")
@RequiredArgsConstructor
@Tag(name = "Disease Detection APIs", description = "Endpoints for uploading crop images, AI plant pathology detection, and diagnostic history")
@SecurityRequirement(name = "bearerAuth")
public class DiseaseScanController {

    private final DiseaseScanService diseaseScanService;

    @PostMapping(value = "/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Scan Crop Disease Image", description = "Uploads a crop image (JPG, PNG, WEBP) to Cloudinary and performs AI disease diagnosis.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Disease scan completed successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid image file, unsupported format, or file size > 5MB")
    })
    public ResponseEntity<ApiResponse<DiseaseScanResponse>> scanDisease(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Parameter(description = "Crop image file to upload and scan", required = true) @RequestParam("image") MultipartFile image) {
        DiseaseScanResponse response = diseaseScanService.scanDisease(currentUser.getId(), image);
        return new ResponseEntity<>(ApiResponse.success("Disease scan completed successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/history")
    @Operation(summary = "Get Disease Scan History", description = "Fetches past disease diagnostic scan records for the authenticated farmer.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Scan history retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<DiseaseScanResponse>>> getScanHistory(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<DiseaseScanResponse> history = diseaseScanService.getScanHistory(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Scan history retrieved successfully", history));
    }

    @GetMapping("/scan/{scanId}")
    @Operation(summary = "Get Specific Scan Details", description = "Retrieves a specific disease scan diagnosis result by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Scan details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Scan record not found or unauthorized")
    })
    public ResponseEntity<ApiResponse<DiseaseScanResponse>> getScanById(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Parameter(description = "ID of the disease scan record", example = "10") @PathVariable Long scanId) {
        DiseaseScanResponse scan = diseaseScanService.getScanById(currentUser.getId(), scanId);
        return ResponseEntity.ok(ApiResponse.success("Scan details retrieved successfully", scan));
    }
}
