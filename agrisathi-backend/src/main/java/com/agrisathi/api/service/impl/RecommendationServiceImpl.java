package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.FertilizerRecommendationRequest;
import com.agrisathi.api.dto.response.FertilizerRecommendationResponse;
import com.agrisathi.api.service.RecommendationService;
import org.springframework.stereotype.Service;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Override
    public FertilizerRecommendationResponse recommendFertilizer(FertilizerRecommendationRequest request) {
        String fertilizer = "NPK 20:20:20";
        String quantity = "50kg/hectare";

        if (request.getDisease() != null && request.getDisease().equalsIgnoreCase("Leaf Rust")) {
            fertilizer = "Copper Oxychloride + NPK 19:19:19";
            quantity = "45kg/hectare";
        }

        return FertilizerRecommendationResponse.builder()
                .fertilizer(fertilizer)
                .quantity(quantity)
                .build();
    }
}
