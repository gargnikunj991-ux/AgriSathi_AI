package com.agrisathi.api.dto.request;

import jakarta.validation.constraints.*;
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
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    private String description;

    @NotBlank(message = "State is required (use 'All India' or specific state name)")
    @Size(min = 2, max = 100, message = "State must be between 2 and 100 characters")
    private String state;

    @Size(max = 200, message = "Target crop maximum length is 200 characters")
    private String targetCrop;

    @Size(max = 2000, message = "Eligibility text maximum length is 2000 characters")
    private String eligibility;

    @Size(max = 100, message = "Category maximum length is 100 characters")
    private String category;

    @Size(max = 200, message = "Benefit amount maximum length is 200 characters")
    private String benefitAmount;

    @PositiveOrZero(message = "Minimum farm size must be 0 or greater")
    private Double minFarmSize;

    @Positive(message = "Maximum farm size must be greater than 0")
    private Double maxFarmSize;

    @Size(max = 500, message = "Apply link maximum length is 500 characters")
    @Pattern(regexp = "^(https?://.*|)$", message = "Apply link must be a valid HTTP or HTTPS URL")
    private String applyLink;

    private Boolean isActive;
}
