package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.CropAdviceRequest;
import com.agrisathi.api.dto.request.FertilizerRecommendationRequest;
import com.agrisathi.api.dto.response.CropAdviceResponse;
import com.agrisathi.api.dto.response.FertilizerRecommendationResponse;

public interface RecommendationService {
    FertilizerRecommendationResponse recommendFertilizer(FertilizerRecommendationRequest request);
    CropAdviceResponse recommendCropAdvice(CropAdviceRequest request);
}
