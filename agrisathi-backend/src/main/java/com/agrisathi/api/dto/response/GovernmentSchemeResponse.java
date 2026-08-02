package com.agrisathi.api.dto.response;

import com.agrisathi.api.model.entity.GovernmentScheme;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class GovernmentSchemeResponse {

    private Long id;
    private String title;
    private String description;
    private String state;
    private String targetCrop;
    private String eligibility;
    private String category;
    private String benefitAmount;
    private Double minFarmSize;
    private Double maxFarmSize;
    private String applyLink;
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
