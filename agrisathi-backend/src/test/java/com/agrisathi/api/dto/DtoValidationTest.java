package com.agrisathi.api.dto;

import com.agrisathi.api.dto.request.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("RegisterRequest Validation - Should fail on invalid email, phone number, and missing role")
    void testRegisterRequestValidationInvalid() {
        RegisterRequest request = RegisterRequest.builder()
                .name("A")
                .email("invalid-email")
                .password("short")
                .phone("12345")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("RegisterRequest Validation - Should pass on valid inputs with mandatory role selection")
    void testRegisterRequestValidationValid() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Ramesh Kumar")
                .email("ramesh@gmail.com")
                .password("Password@123")
                .phone("9876543210")
                .role("FARMER")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("MarketplaceRequest Validation - Should fail on invalid image URL or price")
    void testMarketplaceRequestValidationInvalid() {
        MarketplaceRequest request = MarketplaceRequest.builder()
                .cropName("R")
                .imageUrl("ftp://invalid-url.com")
                .quantity(BigDecimal.valueOf(-5))
                .price(BigDecimal.valueOf(0))
                .unit("")
                .location("")
                .build();

        Set<ConstraintViolation<MarketplaceRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("ContactSellerRequest Validation - Should validate buyer email and phone pattern")
    void testContactSellerValidation() {
        ContactSellerRequest invalid = ContactSellerRequest.builder()
                .message("Hi")
                .buyerPhone("123")
                .buyerEmail("bad-email")
                .build();

        Set<ConstraintViolation<ContactSellerRequest>> violations = validator.validate(invalid);
        assertThat(violations).hasSize(3);

        ContactSellerRequest valid = ContactSellerRequest.builder()
                .message("Interested in buying 100kg rice.")
                .buyerPhone("9876543210")
                .buyerEmail("buyer@gmail.com")
                .build();

        Set<ConstraintViolation<ContactSellerRequest>> validViolations = validator.validate(valid);
        assertThat(validViolations).isEmpty();
    }

    @Test
    @DisplayName("GovernmentSchemeRequest Validation - Should validate title length and apply link URL")
    void testGovernmentSchemeValidation() {
        GovernmentSchemeRequest request = GovernmentSchemeRequest.builder()
                .title("PM")
                .description("Short")
                .state("U")
                .applyLink("not-a-url")
                .build();

        Set<ConstraintViolation<GovernmentSchemeRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }
}
