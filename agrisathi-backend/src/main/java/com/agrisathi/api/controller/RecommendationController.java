package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.FertilizerRecommendationRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.FertilizerRecommendationResponse;
import com.agrisathi.api.service.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/fertilizer")
    public ResponseEntity<ApiResponse<FertilizerRecommendationResponse>> getFertilizerRecommendation(
            @Valid @RequestBody FertilizerRecommendationRequest request) {
        FertilizerRecommendationResponse recommendation = recommendationService.recommendFertilizer(request);
        return ResponseEntity.ok(ApiResponse.success("Fertilizer recommendation retrieved successfully", recommendation));
    }
}
