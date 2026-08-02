package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.GovernmentSchemeRequest;
import com.agrisathi.api.dto.response.SchemeRecommendationResponse;
import com.agrisathi.api.model.entity.FarmerProfile;
import com.agrisathi.api.model.entity.GovernmentScheme;
import com.agrisathi.api.repository.FarmerProfileRepository;
import com.agrisathi.api.repository.GovernmentSchemeRepository;
import com.agrisathi.api.service.impl.GovernmentSchemeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GovernmentSchemeServiceTest {

    @Mock
    private GovernmentSchemeRepository schemeRepository;

    @Mock
    private FarmerProfileRepository farmerProfileRepository;

    private GovernmentSchemeServiceImpl schemeService;

    @BeforeEach
    void setUp() {
        schemeService = new GovernmentSchemeServiceImpl(schemeRepository, farmerProfileRepository);
    }

    @Test
    @DisplayName("getSchemes - Should return filtered schemes")
    void testGetSchemesFiltered() {
        GovernmentScheme scheme = GovernmentScheme.builder()
                .title("PM-KISAN")
                .state("All India")
                .targetCrop("All Crops")
                .build();

        given(schemeRepository.filterSchemes("Uttarakhand", "Rice")).willReturn(List.of(scheme));

        List<GovernmentScheme> result = schemeService.getSchemes("Uttarakhand", "Rice");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("PM-KISAN");
    }

    @Test
    @DisplayName("recommendSchemes - Should recommend schemes matching farmer profile")
    void testRecommendSchemesWithProfile() {
        FarmerProfile profile = FarmerProfile.builder()
                .state("Uttarakhand")
                .mainCrop("Rice")
                .farmSize(java.math.BigDecimal.valueOf(3.0))
                .build();

        GovernmentScheme scheme1 = GovernmentScheme.builder()
                .id(1L)
                .title("Uttarakhand Hill Crop Scheme")
                .state("Uttarakhand")
                .targetCrop("Rice")
                .minFarmSize(0.5)
                .maxFarmSize(5.0)
                .isActive(true)
                .build();

        GovernmentScheme scheme2 = GovernmentScheme.builder()
                .id(2L)
                .title("PM-KISAN")
                .state("All India")
                .targetCrop("All Crops")
                .isActive(true)
                .build();

        given(farmerProfileRepository.findByUserId(10L)).willReturn(Optional.of(profile));
        given(schemeRepository.findByIsActiveTrue()).willReturn(List.of(scheme1, scheme2));

        List<SchemeRecommendationResponse> recommendations = schemeService.recommendSchemes(10L, null, null, null);

        assertThat(recommendations).isNotEmpty();
        assertThat(recommendations.get(0).getMatchScore()).isGreaterThanOrEqualTo(80);
    }

    @Test
    @DisplayName("createScheme - Should save and return new scheme")
    void testCreateScheme() {
        GovernmentSchemeRequest request = GovernmentSchemeRequest.builder()
                .title("New Scheme")
                .description("Desc")
                .state("Uttarakhand")
                .targetCrop("Wheat")
                .build();

        GovernmentScheme saved = GovernmentScheme.builder()
                .id(1L)
                .title("New Scheme")
                .state("Uttarakhand")
                .targetCrop("Wheat")
                .build();

        given(schemeRepository.save(any(GovernmentScheme.class))).willReturn(saved);

        GovernmentScheme result = schemeService.createScheme(request);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Scheme");
        verify(schemeRepository).save(any(GovernmentScheme.class));
    }
}
