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
public class FertilizerRecommendationResponse {
    private String fertilizer;
    private String quantity;
    private String applicationMethod;
    private String applicationTiming;
    private List<String> organicAlternatives;
}
