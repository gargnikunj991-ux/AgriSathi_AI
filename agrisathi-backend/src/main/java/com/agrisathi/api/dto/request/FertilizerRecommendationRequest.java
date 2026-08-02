package com.agrisathi.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FertilizerRecommendationRequest {

    @NotBlank(message = "Crop is required")
    @Size(min = 2, max = 100, message = "Crop name must be between 2 and 100 characters")
    private String crop;

    @NotBlank(message = "Soil type is required")
    @Size(min = 2, max = 50, message = "Soil type must be between 2 and 50 characters")
    private String soilType;

    @Size(max = 100, message = "Disease maximum 100 characters")
    private String disease;
}
