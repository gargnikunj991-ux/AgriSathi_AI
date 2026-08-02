package com.agrisathi.api.dto.request;

import com.agrisathi.api.model.enums.CropStatus;
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
public class CropRequest {

    @NotBlank(message = "Crop name is required")
    @Size(min = 2, max = 100, message = "Crop name must be between 2 and 100 characters")
    private String cropName;

    @NotNull(message = "Sowing date is required")
    private LocalDate sowingDate;

    private LocalDate harvestDate;

    private CropStatus status;
}
