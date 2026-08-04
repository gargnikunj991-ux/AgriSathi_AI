package com.agrisathi.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CropAdviceResponse {
    private String state;
    private String district;
    private String soilType;
    private String season;
    private List<String> recommendedCrops;
    private String optimalSowingWindow;
    private String expectedYieldPerAcre;
    private String estimatedProfitPerAcre;
    private String irrigationAdvice;
    private String pestWarning;
}
