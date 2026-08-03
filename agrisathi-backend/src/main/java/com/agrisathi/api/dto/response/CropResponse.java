package com.agrisathi.api.dto.response;

import com.agrisathi.api.model.entity.Crop;
import com.agrisathi.api.model.enums.CropStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Crop entity details response payload")
public class CropResponse {

    @Schema(description = "Unique crop ID", example = "1")
    private Long id;

    @Schema(description = "User ID of the farmer owner", example = "10")
    private Long userId;

    @Schema(description = "Crop name", example = "Basmati Rice")
    private String cropName;

    @Schema(description = "Sowing date", example = "2026-06-15")
    private LocalDate sowingDate;

    @Schema(description = "Harvest date", example = "2026-11-20")
    private LocalDate harvestDate;

    @Schema(description = "Crop growth status", example = "GROWING")
    private CropStatus status;

    @Schema(description = "Record creation timestamp", example = "2026-08-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Record last update timestamp", example = "2026-08-02T14:30:00")
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
