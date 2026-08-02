package com.agrisathi.api.dto.request;

import com.agrisathi.api.model.enums.ListingStatus;
import jakarta.validation.constraints.*;
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
    @Size(min = 2, max = 100, message = "Crop name must be between 2 and 100 characters")
    private String cropName;

    @Size(max = 2000, message = "Description maximum length is 2000 characters")
    private String description;

    @Size(max = 500, message = "Image URL maximum length is 500 characters")
    @Pattern(regexp = "^(https?://.*|)$", message = "Image URL must be a valid HTTP or HTTPS URL")
    private String imageUrl;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    @DecimalMax(value = "1000000.0", message = "Quantity cannot exceed 1,000,000 units")
    private BigDecimal quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    @DecimalMax(value = "1000000.0", message = "Price cannot exceed ₹1,000,000 per unit")
    private BigDecimal price;

    @NotBlank(message = "Unit is required")
    @Size(min = 1, max = 20, message = "Unit must be between 1 and 20 characters")
    private String unit;

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 150, message = "Location must be between 2 and 150 characters")
    private String location;

    private ListingStatus status;
}
