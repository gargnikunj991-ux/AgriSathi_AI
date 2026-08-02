package com.agrisathi.api.dto.request;

import com.agrisathi.api.model.enums.ListingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketplaceRequest {

    @NotBlank(message = "Crop name is required")
    private String cropName;

    private String description;

    private String imageUrl;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotBlank(message = "Location is required")
    private String location;

    private ListingStatus status;
}
