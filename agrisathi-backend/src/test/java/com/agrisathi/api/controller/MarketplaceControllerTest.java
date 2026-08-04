package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.ContactSellerRequest;
import com.agrisathi.api.dto.request.MarketplaceRequest;
import com.agrisathi.api.dto.response.SellerContactResponse;
import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.model.enums.ListingStatus;
import com.agrisathi.api.security.JwtAuthenticationEntryPoint;
import com.agrisathi.api.security.JwtTokenProvider;
import com.agrisathi.api.security.UserDetailsServiceImpl;
import com.agrisathi.api.service.MarketplaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MarketplaceController.class)
@AutoConfigureMockMvc(addFilters = false)
class MarketplaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MarketplaceService marketplaceService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @DisplayName("GET /api/v1/marketplace/listings - Should browse listings with optional filters")
    void testBrowseListings() throws Exception {
        MarketplaceListing listing = MarketplaceListing.builder()
                .id(1L)
                .cropName("Basmati Rice")
                .quantity(BigDecimal.valueOf(100))
                .price(BigDecimal.valueOf(45.00))
                .unit("kg")
                .location("Dehradun")
                .status(ListingStatus.AVAILABLE)
                .build();

        given(marketplaceService.browseListings("Rice", "Dehradun", BigDecimal.valueOf(10), BigDecimal.valueOf(100)))
                .willReturn(List.of(listing));

        mockMvc.perform(get("/api/v1/marketplace/listings")
                        .param("cropName", "Rice")
                        .param("location", "Dehradun")
                        .param("minPrice", "10")
                        .param("maxPrice", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].cropName").value("Basmati Rice"))
                .andExpect(jsonPath("$.data[0].location").value("Dehradun"));
    }

    @Test
    @DisplayName("GET /api/v1/marketplace/listings/search - Should search listings by query")
    void testSearchListings() throws Exception {
        MarketplaceListing listing = MarketplaceListing.builder()
                .id(2L)
                .cropName("Wheat")
                .quantity(BigDecimal.valueOf(500))
                .price(BigDecimal.valueOf(25.00))
                .unit("kg")
                .location("Haridwar")
                .build();

        given(marketplaceService.searchListings("Wheat")).willReturn(List.of(listing));

        mockMvc.perform(get("/api/v1/marketplace/listings/search")
                        .param("query", "Wheat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].cropName").value("Wheat"));
    }

    @Test
    @DisplayName("POST /api/v1/marketplace/listings/{id}/contact - Should return seller contact info")
    void testContactSeller() throws Exception {
        ContactSellerRequest request = ContactSellerRequest.builder()
                .message("I want to buy 50kg rice")
                .buyerName("John Doe")
                .buyerPhone("9876543210")
                .build();

        SellerContactResponse response = SellerContactResponse.builder()
                .listingId(1L)
                .cropName("Basmati Rice")
                .sellerName("Farmer Ramesh")
                .sellerPhone("9998887770")
                .sellerEmail("ramesh@agrisathi.com")
                .contactStatus("INQUIRY_SENT")
                .build();

        given(marketplaceService.contactSeller(eq(1L), any(ContactSellerRequest.class), any()))
                .willReturn(response);

        mockMvc.perform(post("/api/v1/marketplace/listings/1/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.sellerName").value("Farmer Ramesh"))
                .andExpect(jsonPath("$.data.contactStatus").value("INQUIRY_SENT"));
    }
}
