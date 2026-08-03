package com.agrisathi.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Fertilizer recommendation request payload")
public class FertilizerRecommendationRequest {

    @NotBlank(message = "Crop is required")
    @Size(min = 2, max = 100, message = "Crop name must be between 2 and 100 characters")
    @Schema(description = "Cultivated crop type", example = "Rice")
    private String crop;

    @NotBlank(message = "Soil type is required")
    @Size(min = 2, max = 50, message = "Soil type must be between 2 and 50 characters")
    @Schema(description = "Soil classification (Alluvial, Black, Clay, Loamy, Sandy)", example = "Loamy")
    private String soilType;

    @Size(max = 100, message = "Disease maximum 100 characters")
    @Schema(description = "Diagnosed crop disease (optional)", example = "Leaf Rust")
    private String disease;
}
