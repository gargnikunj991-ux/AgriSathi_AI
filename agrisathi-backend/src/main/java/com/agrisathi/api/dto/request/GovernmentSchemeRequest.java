package com.agrisathi.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Government scheme catalog creation/update request payload")
public class GovernmentSchemeRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    @Schema(description = "Scheme official title", example = "Pradhan Mantri Kisan Samman Nidhi (PM-KISAN)")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    @Schema(description = "Comprehensive scheme summary and objectives", example = "Direct income support of ₹6,000 per year transferred into bank accounts of eligible landholding farmer families.")
    private String description;

    @NotBlank(message = "State is required (use 'All India' or specific state name)")
    @Size(min = 2, max = 100, message = "State must be between 2 and 100 characters")
    @Schema(description = "Applicable state location or 'All India'", example = "All India")
    private String state;

    @Size(max = 200, message = "Target crop maximum length is 200 characters")
    @Schema(description = "Target crops or 'All Crops'", example = "All Crops")
    private String targetCrop;

    @Size(max = 2000, message = "Eligibility text maximum length is 2000 characters")
    @Schema(description = "Eligibility rules and documentation requirements", example = "All landholding farmer families with cultivable landholding up to 50 acres.")
    private String eligibility;

    @Size(max = 100, message = "Category maximum length is 100 characters")
    @Schema(description = "Scheme category (Income Support, Insurance, Subsidy, Irrigation)", example = "Income Support")
    private String category;

    @Size(max = 200, message = "Benefit amount maximum length is 200 characters")
    @Schema(description = "Financial benefit or subsidy percentage", example = "₹6,000 / year in 3 equal installments")
    private String benefitAmount;

    @PositiveOrZero(message = "Minimum farm size must be 0 or greater")
    @Schema(description = "Minimum eligible farm size in acres", example = "0.1")
    private Double minFarmSize;

    @Positive(message = "Maximum farm size must be greater than 0")
    @Schema(description = "Maximum eligible farm size in acres", example = "50.0")
    private Double maxFarmSize;

    @Size(max = 500, message = "Apply link maximum length is 500 characters")
    @Pattern(regexp = "^(https?://.*|)$", message = "Apply link must be a valid HTTP or HTTPS URL")
    @Schema(description = "Official government portal application URL", example = "https://pmkisan.gov.in/")
    private String applyLink;

    @Schema(description = "Active status flag", example = "true")
    private Boolean isActive;
}
