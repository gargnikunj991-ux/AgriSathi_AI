package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.GovernmentSchemeRequest;
import com.agrisathi.api.dto.response.SchemeRecommendationResponse;
import com.agrisathi.api.model.entity.GovernmentScheme;
import com.agrisathi.api.security.JwtAuthenticationEntryPoint;
import com.agrisathi.api.security.JwtTokenProvider;
import com.agrisathi.api.security.UserDetailsServiceImpl;
import com.agrisathi.api.service.GovernmentSchemeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GovernmentSchemeController.class)
@AutoConfigureMockMvc(addFilters = false)
class GovernmentSchemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GovernmentSchemeService schemeService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @DisplayName("GET /api/v1/government-schemes - Should list schemes with optional filters")
    void testGetSchemes() throws Exception {
        GovernmentScheme scheme = GovernmentScheme.builder()
                .id(1L)
                .title("PM-KISAN")
                .description("Income support scheme")
                .state("All India")
                .targetCrop("All Crops")
                .benefitAmount("₹6,000 / year")
                .build();

        given(schemeService.getSchemes("Uttarakhand", "Rice")).willReturn(List.of(scheme));

        mockMvc.perform(get("/api/v1/government-schemes")
                        .param("state", "Uttarakhand")
                        .param("crop", "Rice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("PM-KISAN"))
                .andExpect(jsonPath("$.data[0].state").value("All India"));
    }

    @Test
    @DisplayName("GET /api/v1/government-schemes/{id} - Should return single scheme")
    void testGetSchemeById() throws Exception {
        GovernmentScheme scheme = GovernmentScheme.builder()
                .id(1L)
                .title("PMFBY Crop Insurance")
                .state("All India")
                .targetCrop("Rice")
                .build();

        given(schemeService.getSchemeById(1L)).willReturn(scheme);

        mockMvc.perform(get("/api/v1/government-schemes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("PMFBY Crop Insurance"));
    }

    @Test
    @DisplayName("GET /api/v1/government-schemes/recommendations - Should return tailored scheme recommendations")
    void testRecommendSchemes() throws Exception {
        GovernmentScheme scheme = GovernmentScheme.builder()
                .id(1L)
                .title("PKVY Organic Farming")
                .state("All India")
                .targetCrop("Organic Vegetables")
                .build();

        SchemeRecommendationResponse rec = SchemeRecommendationResponse.builder()
                .scheme(scheme)
                .matchScore(95)
                .matchReason("State Match (Uttarakhand) | Specific Crop Match (Rice)")
                .build();

        given(schemeService.recommendSchemes(any(), eq("Uttarakhand"), eq("Rice"), eq(2.5)))
                .willReturn(List.of(rec));

        mockMvc.perform(get("/api/v1/government-schemes/recommendations")
                        .param("state", "Uttarakhand")
                        .param("crop", "Rice")
                        .param("farmSize", "2.5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].matchScore").value(95))
                .andExpect(jsonPath("$.data[0].matchReason").value("State Match (Uttarakhand) | Specific Crop Match (Rice)"));
    }

    @Test
    @DisplayName("POST /api/v1/government-schemes - Should create new scheme")
    void testCreateScheme() throws Exception {
        GovernmentSchemeRequest request = GovernmentSchemeRequest.builder()
                .title("New Soil Subsidy")
                .description("Subsidy for soil testing")
                .state("Uttarakhand")
                .targetCrop("All Crops")
                .benefitAmount("₹2,000")
                .build();

        GovernmentScheme created = GovernmentScheme.builder()
                .id(10L)
                .title("New Soil Subsidy")
                .state("Uttarakhand")
                .targetCrop("All Crops")
                .build();

        given(schemeService.createScheme(any(GovernmentSchemeRequest.class))).willReturn(created);

        mockMvc.perform(post("/api/v1/government-schemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.data.title").value("New Soil Subsidy"));
    }
}
