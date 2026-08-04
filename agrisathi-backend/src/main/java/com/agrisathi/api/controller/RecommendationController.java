package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.CropAdviceRequest;
import com.agrisathi.api.dto.request.FertilizerRecommendationRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.CropAdviceResponse;
import com.agrisathi.api.dto.response.FertilizerRecommendationResponse;
import com.agrisathi.api.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendation APIs", description = "Endpoints for fertilizer planning, dosage calculation, and crop cultivation advice")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/fertilizer")
    @Operation(summary = "Recommend Fertilizer", description = "Calculates NPK fertilizer blend, dosage, application timing, and precautions based on crop type, soil, and diagnosed disease.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Fertilizer recommendation retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid crop or soil parameters")
    })
    public ResponseEntity<ApiResponse<FertilizerRecommendationResponse>> getFertilizerRecommendation(
            @Valid @RequestBody FertilizerRecommendationRequest request) {
        FertilizerRecommendationResponse recommendation = recommendationService.recommendFertilizer(request);
        return ResponseEntity.ok(ApiResponse.success("Fertilizer recommendation retrieved successfully", recommendation));
    }

    @PostMapping("/crop-advice")
    @Operation(summary = "Get Crop Cultivation Advisory", description = "Generates comprehensive agricultural advice covering sowing windows, irrigation frequency, pest control, and harvesting practices.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Crop advice generated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    public ResponseEntity<ApiResponse<CropAdviceResponse>> getCropAdvice(
            @Valid @RequestBody CropAdviceRequest request) {
        CropAdviceResponse advice = recommendationService.recommendCropAdvice(request);
        return ResponseEntity.ok(ApiResponse.success("Crop advice generated successfully", advice));
    }
}
