package com.agrisathi.api.dto.request;

import com.agrisathi.api.model.enums.CropStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Crop registration and update request payload")
public class CropRequest {

    @NotBlank(message = "Crop name is required")
    @Size(min = 2, max = 100, message = "Crop name must be between 2 and 100 characters")
    @Schema(description = "Crop name", example = "Basmati Rice")
    private String cropName;

    @NotNull(message = "Sowing date is required")
    @Schema(description = "Date when crop was sown (YYYY-MM-DD)", example = "2026-06-15")
    private LocalDate sowingDate;

    @Schema(description = "Expected or actual harvest date (YYYY-MM-DD)", example = "2026-11-20")
    private LocalDate harvestDate;

    @Schema(description = "Growth status of the crop (PLANTED, GROWING, HARVESTED, FAILED)", example = "GROWING")
    private CropStatus status;
}
