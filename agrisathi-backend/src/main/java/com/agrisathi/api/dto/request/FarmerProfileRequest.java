package com.agrisathi.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Farmer profile metadata request payload")
public class FarmerProfileRequest {

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 100, message = "State must be between 2 and 100 characters")
    @Schema(description = "State location", example = "Uttarakhand")
    private String state;

    @NotBlank(message = "District is required")
    @Size(min = 2, max = 100, message = "District must be between 2 and 100 characters")
    @Schema(description = "District location", example = "Dehradun")
    private String district;

    @Size(max = 100, message = "Village maximum 100 characters")
    @Schema(description = "Village or sub-district area", example = "Raipur")
    private String village;

    @NotNull(message = "Farm size is required")
    @Positive(message = "Farm size must be greater than 0")
    @DecimalMax(value = "1000.0", message = "Farm size cannot exceed 1000 acres")
    @Schema(description = "Farm land size in acres", example = "2.5")
    private BigDecimal farmSize;

    @NotBlank(message = "Soil type is required")
    @Size(min = 2, max = 50, message = "Soil type must be between 2 and 50 characters")
    @Schema(description = "Primary soil classification (e.g. Alluvial, Black, Clay, Loamy, Sandy)", example = "Loamy")
    private String soilType;

    @NotBlank(message = "Main crop is required")
    @Size(min = 2, max = 100, message = "Main crop must be between 2 and 100 characters")
    @Schema(description = "Main cultivated crop", example = "Rice")
    private String mainCrop;
}
