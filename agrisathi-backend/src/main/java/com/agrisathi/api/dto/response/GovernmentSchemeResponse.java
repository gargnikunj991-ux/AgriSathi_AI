package com.agrisathi.api.dto.response;

import com.agrisathi.api.model.entity.GovernmentScheme;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Government scheme catalog entry response payload")
public class GovernmentSchemeResponse {

    @Schema(description = "Scheme ID", example = "1")
    private Long id;

    @Schema(description = "Scheme title", example = "Pradhan Mantri Fasal Bima Yojana (PMFBY)")
    private String title;

    @Schema(description = "Detailed description", example = "Comprehensive yield index crop insurance covering natural calamities.")
    private String description;

    @Schema(description = "Applicable state location", example = "All India")
    private String state;

    @Schema(description = "Target crops", example = "Rice, Wheat, Commercial Crops")
    private String targetCrop;

    @Schema(description = "Eligibility text", example = "All farmers growing notified crops in notified areas.")
    private String eligibility;

    @Schema(description = "Scheme category", example = "Crop Insurance")
    private String category;

    @Schema(description = "Benefit amount or coverage", example = "Up to 100% Sum Insured")
    private String benefitAmount;

    @Schema(description = "Minimum eligible farm size in acres", example = "0.1")
    private Double minFarmSize;

    @Schema(description = "Maximum eligible farm size in acres", example = "50.0")
    private Double maxFarmSize;

    @Schema(description = "Application portal URL", example = "https://pmfby.gov.in/")
    private String applyLink;

    @Schema(description = "Active status flag", example = "true")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Creation timestamp", example = "2026-08-01 10:00:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last update timestamp", example = "2026-08-02 14:30:00")
    private LocalDateTime updatedAt;

    public static GovernmentSchemeResponse fromEntity(GovernmentScheme scheme) {
        if (scheme == null) return null;
        return GovernmentSchemeResponse.builder()
                .id(scheme.getId())
                .title(scheme.getTitle())
                .description(scheme.getDescription())
                .state(scheme.getState())
                .targetCrop(scheme.getTargetCrop())
                .eligibility(scheme.getEligibility())
                .category(scheme.getCategory())
                .benefitAmount(scheme.getBenefitAmount())
                .minFarmSize(scheme.getMinFarmSize())
                .maxFarmSize(scheme.getMaxFarmSize())
                .applyLink(scheme.getApplyLink())
                .isActive(scheme.getIsActive())
                .createdAt(scheme.getCreatedAt())
                .updatedAt(scheme.getUpdatedAt())
                .build();
    }
}
