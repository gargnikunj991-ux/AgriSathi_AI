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
public class FarmingWeatherSummaryResponse {
    private Double latitude;
    private Double longitude;
    private CurrentWeatherResponse currentWeather;
    private String overallAdvisory;
    private String irrigationAdvice;
    private String sprayingCondition;
    private String frostRisk;
    private String heatStressRisk;
    private String sowingSuitability;
    private String harvestingSuitability;
    private List<String> actionItems;
}
