package com.agrisathi.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 5, max = 1000, message = "Inquiry message must be between 5 and 1000 characters")
    private String message;

    @Size(max = 100, message = "Buyer name maximum length is 100 characters")
    private String buyerName;

    @Pattern(regexp = "^([6-9]\\d{9})?$", message = "Buyer phone must be a valid 10-digit Indian mobile number")
    private String buyerPhone;

    @Email(message = "Buyer email must be a valid email format")
    @Size(max = 120, message = "Buyer email maximum length is 120 characters")
    private String buyerEmail;
}
