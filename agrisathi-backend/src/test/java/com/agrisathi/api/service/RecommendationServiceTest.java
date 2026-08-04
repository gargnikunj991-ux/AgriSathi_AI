package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.CropAdviceRequest;
import com.agrisathi.api.dto.request.FertilizerRecommendationRequest;
import com.agrisathi.api.dto.response.CropAdviceResponse;
import com.agrisathi.api.dto.response.FertilizerRecommendationResponse;
import com.agrisathi.api.service.impl.RecommendationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationServiceTest {

    private RecommendationServiceImpl recommendationService;

    @BeforeEach
    void setUp() {
        recommendationService = new RecommendationServiceImpl();
    }

    @Test
    void testRecommendFertilizer_StandardCrop() {
        FertilizerRecommendationRequest request = FertilizerRecommendationRequest.builder()
                .crop("Rice")
                .soilType("Loamy")
                .build();

        FertilizerRecommendationResponse response = recommendationService.recommendFertilizer(request);

        assertNotNull(response);
        assertNotNull(response.getFertilizer());
        assertNotNull(response.getQuantity());
        assertNotNull(response.getOrganicAlternatives());
        assertFalse(response.getOrganicAlternatives().isEmpty());
    }

    @Test
    void testRecommendFertilizer_LeafRustDisease() {
        FertilizerRecommendationRequest request = FertilizerRecommendationRequest.builder()
                .crop("Wheat")
                .soilType("Clay")
                .disease("Leaf Rust")
                .build();

        FertilizerRecommendationResponse response = recommendationService.recommendFertilizer(request);

        assertNotNull(response);
        assertTrue(response.getFertilizer().contains("Copper Oxychloride"));
    }

    @Test
    void testRecommendCropAdvice_RabiSeason() {
        CropAdviceRequest request = CropAdviceRequest.builder()
                .state("Uttarakhand")
                .district("Dehradun")
                .soilType("Loamy")
                .season("Rabi")
                .waterAvailability("Medium")
                .build();

        CropAdviceResponse response = recommendationService.recommendCropAdvice(request);

        assertNotNull(response);
        assertEquals("Uttarakhand", response.getState());
        assertEquals("Rabi", response.getSeason());
        assertTrue(response.getRecommendedCrops().contains("Wheat (HD-2967)"));
        assertNotNull(response.getOptimalSowingWindow());
        assertNotNull(response.getEstimatedProfitPerAcre());
    }
}
