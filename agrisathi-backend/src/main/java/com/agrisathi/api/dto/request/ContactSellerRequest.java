package com.agrisathi.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactSellerRequest {

    @NotBlank(message = "Inquiry message is required")
    private String message;

    private String buyerName;
    private String buyerPhone;
    private String buyerEmail;
}
