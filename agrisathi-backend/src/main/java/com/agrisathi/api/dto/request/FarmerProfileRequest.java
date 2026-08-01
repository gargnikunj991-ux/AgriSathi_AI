package com.agrisathi.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FarmerProfileRequest {

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "District is required")
    private String district;

    private String village;

    @NotNull(message = "Farm size is required")
    @Positive(message = "Farm size must be greater than 0")
    private BigDecimal farmSize;

    @NotBlank(message = "Soil type is required")
    private String soilType;

    @NotBlank(message = "Main crop is required")
    private String mainCrop;
}
