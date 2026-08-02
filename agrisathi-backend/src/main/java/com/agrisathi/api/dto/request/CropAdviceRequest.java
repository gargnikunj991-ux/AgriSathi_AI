package com.agrisathi.api.dto.request;

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
public class CropAdviceRequest {

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 100, message = "State must be between 2 and 100 characters")
    private String state;

    @NotBlank(message = "District is required")
    @Size(min = 2, max = 100, message = "District must be between 2 and 100 characters")
    private String district;

    @NotBlank(message = "Soil type is required")
    @Size(min = 2, max = 50, message = "Soil type must be between 2 and 50 characters")
    private String soilType;

    @Size(max = 50, message = "Season maximum 50 characters")
    private String season;

    @Positive(message = "Farm size must be greater than 0")
    @DecimalMax(value = "1000.0", message = "Farm size cannot exceed 1000 acres")
    private Double farmSize;

    @Size(max = 50, message = "Water availability maximum 50 characters")
    private String waterAvailability;
}
