package com.agrisathi.api.dto.response;

import com.agrisathi.api.model.entity.Crop;
import com.agrisathi.api.model.enums.CropStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropResponse {

    private Long id;
    private Long userId;
    private String cropName;
    private LocalDate sowingDate;
    private LocalDate harvestDate;
    private CropStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CropResponse fromEntity(Crop crop) {
        if (crop == null) return null;
        return CropResponse.builder()
                .id(crop.getId())
                .userId(crop.getUser() != null ? crop.getUser().getId() : null)
                .cropName(crop.getCropName())
                .sowingDate(crop.getSowingDate())
                .harvestDate(crop.getHarvestDate())
                .status(crop.getStatus())
                .createdAt(crop.getCreatedAt())
                .updatedAt(crop.getUpdatedAt())
                .build();
    }
}
