package com.agrisathi.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FertilizerRecommendationRequest {

    @NotBlank(message = "Crop is required")
    private String crop;

    @NotBlank(message = "Soil type is required")
    private String soilType;

    private String disease;
}
