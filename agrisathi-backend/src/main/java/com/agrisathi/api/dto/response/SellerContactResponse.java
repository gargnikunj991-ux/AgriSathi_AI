package com.agrisathi.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerContactResponse {
    private Long listingId;
    private String cropName;
    private BigDecimal quantity;
    private BigDecimal price;
    private String unit;
    private String location;
    private String sellerName;
    private String sellerPhone;
    private String sellerEmail;
    private String contactStatus;
    private String formattedInquiry;
}
