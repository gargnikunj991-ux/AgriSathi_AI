package com.agrisathi.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GovernmentSchemeRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "State is required (use 'All India' or specific state name)")
    private String state;

    private String targetCrop;

    private String eligibility;

    private String category;

    private String benefitAmount;

    private Double minFarmSize;

    private Double maxFarmSize;

    private String applyLink;

    private Boolean isActive;
}
