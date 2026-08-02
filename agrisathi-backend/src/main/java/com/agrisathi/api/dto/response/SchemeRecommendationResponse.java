package com.agrisathi.api.dto.response;

import com.agrisathi.api.model.entity.GovernmentScheme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchemeRecommendationResponse {
    private GovernmentScheme scheme;
    private Integer matchScore;
    private String matchReason;
}
