package com.agrisathi.api.dto.request;

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
public class FarmerProfileRequest {

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 100, message = "State must be between 2 and 100 characters")
    private String state;

    @NotBlank(message = "District is required")
    @Size(min = 2, max = 100, message = "District must be between 2 and 100 characters")
    private String district;

    @Size(max = 100, message = "Village maximum 100 characters")
    private String village;

    @NotNull(message = "Farm size is required")
    @Positive(message = "Farm size must be greater than 0")
    @DecimalMax(value = "1000.0", message = "Farm size cannot exceed 1000 acres")
    private BigDecimal farmSize;

    @NotBlank(message = "Soil type is required")
    @Size(min = 2, max = 50, message = "Soil type must be between 2 and 50 characters")
    private String soilType;

    @NotBlank(message = "Main crop is required")
    @Size(min = 2, max = 100, message = "Main crop must be between 2 and 100 characters")
    private String mainCrop;
}
