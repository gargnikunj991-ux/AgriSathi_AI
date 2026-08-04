package com.agrisathi.api.dto.response;

import com.agrisathi.api.model.entity.FarmerProfile;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FarmerProfileResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String state;
    private String district;
    private String village;
    private BigDecimal farmSize;
    private String soilType;
    private String mainCrop;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static FarmerProfileResponse fromEntity(FarmerProfile profile) {
        if (profile == null) return null;
        return FarmerProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser() != null ? profile.getUser().getId() : null)
                .userName(profile.getUser() != null ? profile.getUser().getName() : null)
                .userEmail(profile.getUser() != null ? profile.getUser().getEmail() : null)
                .userPhone(profile.getUser() != null ? profile.getUser().getPhone() : null)
                .state(profile.getState())
                .district(profile.getDistrict())
                .village(profile.getVillage())
                .farmSize(profile.getFarmSize())
                .soilType(profile.getSoilType())
                .mainCrop(profile.getMainCrop())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
