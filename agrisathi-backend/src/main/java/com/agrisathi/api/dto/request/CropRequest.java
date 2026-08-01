package com.agrisathi.api.dto.request;

import com.agrisathi.api.model.enums.CropStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CropRequest {

    @NotBlank(message = "Crop name is required")
    @Size(max = 100, message = "Crop name maximum 100 characters")
    private String cropName;

    @NotNull(message = "Sowing date is required")
    private LocalDate sowingDate;

    private LocalDate harvestDate;

    private CropStatus status;
}

