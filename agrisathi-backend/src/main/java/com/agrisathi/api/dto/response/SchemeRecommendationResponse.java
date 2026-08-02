package com.agrisathi.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchemeRecommendationResponse {
    private GovernmentSchemeResponse scheme;
    private Integer matchScore;
    private String matchReason;
}
