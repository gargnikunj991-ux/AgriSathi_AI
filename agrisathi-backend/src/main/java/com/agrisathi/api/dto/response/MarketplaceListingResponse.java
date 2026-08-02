package com.agrisathi.api.dto.response;

import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.model.enums.ListingStatus;
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
public class MarketplaceListingResponse {

    private Long id;
    private Long sellerId;
    private String sellerName;
    private String sellerPhone;
    private String sellerEmail;
    private String cropName;
    private String description;
    private String imageUrl;
    private BigDecimal quantity;
    private BigDecimal price;
    private String unit;
    private String location;
    private ListingStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
