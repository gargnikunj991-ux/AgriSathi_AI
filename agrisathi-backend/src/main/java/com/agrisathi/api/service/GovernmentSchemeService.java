package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.GovernmentSchemeRequest;
import com.agrisathi.api.dto.response.SchemeRecommendationResponse;
import com.agrisathi.api.model.entity.GovernmentScheme;

import java.util.List;

public interface GovernmentSchemeService {
    List<GovernmentScheme> getSchemes(String state, String crop);
    GovernmentScheme getSchemeById(Long id);
    List<SchemeRecommendationResponse> recommendSchemes(Long userId, String state, String crop, Double farmSize);
    GovernmentScheme createScheme(GovernmentSchemeRequest request);
    GovernmentScheme updateScheme(Long id, GovernmentSchemeRequest request);
    void deleteScheme(Long id);
}
