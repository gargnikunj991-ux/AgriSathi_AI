package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.FertilizerRecommendationRequest;
import com.agrisathi.api.dto.response.FertilizerRecommendationResponse;

public interface RecommendationService {
    FertilizerRecommendationResponse recommendFertilizer(FertilizerRecommendationRequest request);
}
