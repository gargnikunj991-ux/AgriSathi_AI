package com.agrisathi.api.dto.response;

import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.model.enums.ListingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Produce marketplace listing details response payload")
public class MarketplaceListingResponse {

    @Schema(description = "Listing ID", example = "1")
    private Long id;

    @Schema(description = "Seller user ID", example = "5")
    private Long sellerId;

    @Schema(description = "Seller full name", example = "Ramesh Kumar")
    private String sellerName;

    @Schema(description = "Seller phone number", example = "9876543210")
    private String sellerPhone;

    @Schema(description = "Seller email address", example = "ramesh@agrisathi.com")
    private String sellerEmail;

    @Schema(description = "Crop produce name", example = "Organic Basmati Rice")
    private String cropName;

    @Schema(description = "Detailed produce description", example = "Freshly harvested organic Basmati rice from Dehradun valley.")
    private String description;

    @Schema(description = "Cloudinary image URL", example = "https://res.cloudinary.com/agrisathi/image/upload/v123/rice.jpg")
    private String imageUrl;

    @Schema(description = "Available quantity", example = "500")
    private BigDecimal quantity;

    @Schema(description = "Price per unit in INR (₹)", example = "45.00")
    private BigDecimal price;

    @Schema(description = "Measurement unit", example = "kg")
    private String unit;

    @Schema(description = "Location / District", example = "Dehradun, Uttarakhand")
    private String location;

    @Schema(description = "Listing availability status", example = "AVAILABLE")
    private ListingStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Creation timestamp", example = "2026-08-01 10:00:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last update timestamp", example = "2026-08-02 14:30:00")
    private LocalDateTime updatedAt;

    public static MarketplaceListingResponse fromEntity(MarketplaceListing listing) {
        if (listing == null) return null;
        return MarketplaceListingResponse.builder()
                .id(listing.getId())
                .sellerId(listing.getUser() != null ? listing.getUser().getId() : null)
                .sellerName(listing.getUser() != null ? listing.getUser().getName() : null)
                .sellerPhone(listing.getUser() != null ? listing.getUser().getPhone() : null)
                .sellerEmail(listing.getUser() != null ? listing.getUser().getEmail() : null)
                .cropName(listing.getCropName())
                .description(listing.getDescription())
                .imageUrl(listing.getImageUrl())
                .quantity(listing.getQuantity())
                .price(listing.getPrice())
                .unit(listing.getUnit())
                .location(listing.getLocation())
                .status(listing.getStatus())
                .createdAt(listing.getCreatedAt())
                .updatedAt(listing.getUpdatedAt())
                .build();
    }
}
