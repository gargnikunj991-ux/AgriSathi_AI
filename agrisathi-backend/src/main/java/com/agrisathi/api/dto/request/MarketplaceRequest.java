package com.agrisathi.api.dto.request;

import com.agrisathi.api.model.enums.ListingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Produce marketplace listing creation and update request payload")
public class MarketplaceRequest {

    @NotBlank(message = "Crop name is required")
    @Size(min = 2, max = 100, message = "Crop name must be between 2 and 100 characters")
    @Schema(description = "Name of crop produce for sale", example = "Organic Basmati Rice")
    private String cropName;

    @Size(max = 2000, message = "Description maximum length is 2000 characters")
    @Schema(description = "Detailed description of quality, harvest date, and packaging", example = "Freshly harvested organic Basmati rice from Dehradun valley. No artificial pesticides used.")
    private String description;

    @Size(max = 500, message = "Image URL maximum length is 500 characters")
    @Pattern(regexp = "^(https?://.*|)$", message = "Image URL must be a valid HTTP or HTTPS URL")
    @Schema(description = "Cloudinary image URL for produce photo", example = "https://res.cloudinary.com/agrisathi/image/upload/v123456/rice.jpg")
    private String imageUrl;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    @DecimalMax(value = "1000000.0", message = "Quantity cannot exceed 1,000,000 units")
    @Schema(description = "Total available quantity for sale", example = "500")
    private BigDecimal quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    @DecimalMax(value = "1000000.0", message = "Price cannot exceed ₹1,000,000 per unit")
    @Schema(description = "Price per unit in INR (₹)", example = "45.00")
    private BigDecimal price;

    @NotBlank(message = "Unit is required")
    @Size(min = 1, max = 20, message = "Unit must be between 1 and 20 characters")
    @Schema(description = "Measurement unit (kg, quintal, ton, crate)", example = "kg")
    private String unit;

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 150, message = "Location must be between 2 and 150 characters")
    @Schema(description = "District or pickup location", example = "Dehradun, Uttarakhand")
    private String location;

    @Schema(description = "Listing status (AVAILABLE, SOLD, RESERVED)", example = "AVAILABLE")
    private ListingStatus status;
}
