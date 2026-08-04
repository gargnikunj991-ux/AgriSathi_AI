package com.agrisathi.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Crop cultivation advisory request payload")
public class CropAdviceRequest {

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 100, message = "State must be between 2 and 100 characters")
    @Schema(description = "State location", example = "Uttarakhand")
    private String state;

    @NotBlank(message = "District is required")
    @Size(min = 2, max = 100, message = "District must be between 2 and 100 characters")
    @Schema(description = "District location", example = "Dehradun")
    private String district;

    @NotBlank(message = "Soil type is required")
    @Size(min = 2, max = 50, message = "Soil type must be between 2 and 50 characters")
    @Schema(description = "Soil classification (Alluvial, Black, Clay, Loamy, Sandy)", example = "Loamy")
    private String soilType;

    @Size(max = 50, message = "Season maximum 50 characters")
    @Schema(description = "Farming season (Kharif, Rabi, Zaid)", example = "Kharif")
    private String season;

    @Positive(message = "Farm size must be greater than 0")
    @DecimalMax(value = "1000.0", message = "Farm size cannot exceed 1000 acres")
    @Schema(description = "Farm size in acres", example = "2.5")
    private Double farmSize;

    @Size(max = 50, message = "Water availability maximum 50 characters")
    @Schema(description = "Irrigation / water availability (High, Medium, Low)", example = "High")
    private String waterAvailability;
}
