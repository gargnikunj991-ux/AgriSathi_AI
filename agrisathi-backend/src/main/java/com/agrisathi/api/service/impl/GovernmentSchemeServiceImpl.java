package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.GovernmentSchemeRequest;
import com.agrisathi.api.dto.response.SchemeRecommendationResponse;
import com.agrisathi.api.exception.ResourceNotFoundException;
import com.agrisathi.api.model.entity.FarmerProfile;
import com.agrisathi.api.model.entity.GovernmentScheme;
import com.agrisathi.api.repository.FarmerProfileRepository;
import com.agrisathi.api.repository.GovernmentSchemeRepository;
import com.agrisathi.api.service.GovernmentSchemeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GovernmentSchemeServiceImpl implements GovernmentSchemeService {

    private final GovernmentSchemeRepository schemeRepository;
    private final FarmerProfileRepository farmerProfileRepository;

    @PostConstruct
    public void initDefaultSchemes() {
        try {
            if (schemeRepository.count() == 0) {
                log.info("Seeding initial government schemes database...");
                List<GovernmentScheme> defaultSchemes = List.of(
                        GovernmentScheme.builder()
                                .title("Pradhan Mantri Kisan Samman Nidhi (PM-KISAN)")
                                .description("Direct income support of ₹6,000 per year in three equal installments to all landholding farmer families across India.")
                                .state("All India")
                                .targetCrop("All Crops")
                                .eligibility("All landholding farmer families with cultivable landholding.")
                                .category("Income Support / Subsidy")
                                .benefitAmount("₹6,000 / year")
                                .minFarmSize(0.1)
                                .maxFarmSize(50.0)
                                .applyLink("https://pmkisan.gov.in/")
                                .isActive(true)
                                .build(),

                        GovernmentScheme.builder()
                                .title("Pradhan Mantri Fasal Bima Yojana (PMFBY)")
                                .description("Comprehensive crop insurance against natural calamities, pests, and diseases with uniform low premium rates (1.5% - 2% for Kharif/Rabi crops).")
                                .state("All India")
                                .targetCrop("Rice, Wheat, Pulses, Commercial Crops")
                                .eligibility("Farmers growing notified crops in notified areas including sharecroppers and tenant farmers.")
                                .category("Crop Insurance")
                                .benefitAmount("Up to 100% Sum Insured for Crop Loss")
                                .minFarmSize(0.1)
                                .maxFarmSize(100.0)
                                .applyLink("https://pmfby.gov.in/")
                                .isActive(true)
                                .build(),

                        GovernmentScheme.builder()
                                .title("Paramparagat Krishi Vikas Yojana (PKVY)")
                                .description("Financial assistance for organic farming, cluster formation, organic inputs, soil health management, and PGS certification.")
                                .state("All India")
                                .targetCrop("Organic Vegetables, Pulses, Grains, Spices")
                                .eligibility("Farmers forming clusters of 50 or more acres practicing organic farming methods.")
                                .category("Organic Farming Support")
                                .benefitAmount("₹50,000 / hectare over 3 years")
                                .minFarmSize(0.5)
                                .maxFarmSize(10.0)
                                .applyLink("https://pgsindia-ncof.gov.in/pkvy/index.aspx")
                                .isActive(true)
                                .build(),

                        GovernmentScheme.builder()
                                .title("Sub-Mission on Agricultural Mechanization (SMAM)")
                                .description("Subsidy assistance for purchasing modern farm machinery, tractors, power tillers, and setting up Custom Hiring Centers (CHCs).")
                                .state("All India")
                                .targetCrop("All Crops")
                                .eligibility("Small, marginal, SC/ST, and women farmers given priority for machinery procurement.")
                                .category("Equipment Financing")
                                .benefitAmount("40% - 50% Financial Subsidy")
                                .minFarmSize(0.1)
                                .maxFarmSize(20.0)
                                .applyLink("https://agrimachinery.nic.in/")
                                .isActive(true)
                                .build(),

                        GovernmentScheme.builder()
                                .title("Uttarakhand State Hill Horticulture & Polyhouse Subsidy Scheme")
                                .description("Special state subsidy for setting up hi-tech polyhouses, drip irrigation, and cold storage for high-value hill crops in Uttarakhand.")
                                .state("Uttarakhand")
                                .targetCrop("Fruits, Vegetables, Flowers, Spices")
                                .eligibility("Resident farmers of Uttarakhand engaged in horticulture or polyhouse farming.")
                                .category("Horticulture & Micro-Irrigation")
                                .benefitAmount("Up to 80% Polyhouse Construction Subsidy")
                                .minFarmSize(0.2)
                                .maxFarmSize(5.0)
                                .applyLink("https://shm.uk.gov.in/")
                                .isActive(true)
                                .build()
                );
                schemeRepository.saveAll(defaultSchemes);
                log.info("Successfully seeded {} default government schemes.", defaultSchemes.size());
            }
        } catch (Exception e) {
            log.warn("Could not seed default government schemes: {}", e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<GovernmentScheme> getSchemes(String state, String crop) {
        if (StringUtils.hasText(state) || StringUtils.hasText(crop)) {
            return schemeRepository.filterSchemes(
                    StringUtils.hasText(state) ? state.trim() : null,
                    StringUtils.hasText(crop) ? crop.trim() : null
            );
        }
        return schemeRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public GovernmentScheme getSchemeById(Long id) {
        return schemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Government scheme not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchemeRecommendationResponse> recommendSchemes(Long userId, String stateParam, String cropParam, Double farmSizeParam) {
        String targetState = stateParam;
        String targetCrop = cropParam;
        Double targetFarmSize = farmSizeParam;

        // Fetch farmer profile if userId provided and params missing
        if (userId != null) {
            Optional<FarmerProfile> profileOpt = farmerProfileRepository.findByUserId(userId);
            if (profileOpt.isPresent()) {
                FarmerProfile profile = profileOpt.get();
                if (!StringUtils.hasText(targetState)) targetState = profile.getState();
                if (!StringUtils.hasText(targetCrop)) targetCrop = profile.getMainCrop();
                if (targetFarmSize == null && profile.getFarmSize() != null) targetFarmSize = profile.getFarmSize().doubleValue();
            }
        }

        final String userState = StringUtils.hasText(targetState) ? targetState.trim() : "All India";
        final String userCrop = StringUtils.hasText(targetCrop) ? targetCrop.trim() : "All Crops";
        final double userFarmSize = (targetFarmSize != null && targetFarmSize > 0) ? targetFarmSize : 2.5;

        List<GovernmentScheme> allActive = schemeRepository.findByIsActiveTrue();
        List<SchemeRecommendationResponse> recommendations = new ArrayList<>();

        for (GovernmentScheme scheme : allActive) {
            int matchScore = 0;
            List<String> matchReasons = new ArrayList<>();

            // 1. State Match Evaluation (Max 40 Points)
            String schemeState = scheme.getState() != null ? scheme.getState().trim() : "All India";
            if (schemeState.equalsIgnoreCase(userState)) {
                matchScore += 40;
                matchReasons.add("State Match (" + userState + ")");
            } else if (schemeState.equalsIgnoreCase("All India") || schemeState.equalsIgnoreCase("Central")) {
                matchScore += 35;
                matchReasons.add("Applicable Nationally (All India)");
            }

            // 2. Crop Match Evaluation (Max 35 Points)
            String schemeCrop = scheme.getTargetCrop() != null ? scheme.getTargetCrop().trim() : "All Crops";
            if (schemeCrop.equalsIgnoreCase("All Crops") || schemeCrop.equalsIgnoreCase("All")) {
                matchScore += 35;
                matchReasons.add("Applies to All Crop Varieties");
            } else if (schemeCrop.toLowerCase().contains(userCrop.toLowerCase())) {
                matchScore += 35;
                matchReasons.add("Specific Crop Match (" + userCrop + ")");
            } else {
                matchScore += 10;
            }

            // 3. Farm Size Evaluation (Max 25 Points)
            boolean fitsMin = scheme.getMinFarmSize() == null || userFarmSize >= scheme.getMinFarmSize();
            boolean fitsMax = scheme.getMaxFarmSize() == null || userFarmSize <= scheme.getMaxFarmSize();

            if (fitsMin && fitsMax) {
                matchScore += 25;
                matchReasons.add("Eligible for Farm Size (" + userFarmSize + " Acres)");
            }

            if (matchScore >= 40) {
                recommendations.add(SchemeRecommendationResponse.builder()
                        .scheme(com.agrisathi.api.dto.response.GovernmentSchemeResponse.fromEntity(scheme))
                        .matchScore(matchScore)
                        .matchReason(String.join(" | ", matchReasons))
                        .build());
            }
        }

        return recommendations.stream()
                .sorted(Comparator.comparing(SchemeRecommendationResponse::getMatchScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GovernmentScheme createScheme(GovernmentSchemeRequest request) {
        GovernmentScheme scheme = GovernmentScheme.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .state(request.getState())
                .targetCrop(request.getTargetCrop() != null ? request.getTargetCrop() : "All Crops")
                .eligibility(request.getEligibility())
                .category(request.getCategory())
                .benefitAmount(request.getBenefitAmount())
                .minFarmSize(request.getMinFarmSize())
                .maxFarmSize(request.getMaxFarmSize())
                .applyLink(request.getApplyLink())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        return schemeRepository.save(scheme);
    }

    @Override
    @Transactional
    public GovernmentScheme updateScheme(Long id, GovernmentSchemeRequest request) {
        GovernmentScheme scheme = getSchemeById(id);
        scheme.setTitle(request.getTitle());
        scheme.setDescription(request.getDescription());
        scheme.setState(request.getState());
        if (request.getTargetCrop() != null) scheme.setTargetCrop(request.getTargetCrop());
        scheme.setEligibility(request.getEligibility());
        scheme.setCategory(request.getCategory());
        scheme.setBenefitAmount(request.getBenefitAmount());
        scheme.setMinFarmSize(request.getMinFarmSize());
        scheme.setMaxFarmSize(request.getMaxFarmSize());
        scheme.setApplyLink(request.getApplyLink());
        if (request.getIsActive() != null) scheme.setIsActive(request.getIsActive());

        return schemeRepository.save(scheme);
    }

    @Override
    @Transactional
    public void deleteScheme(Long id) {
        GovernmentScheme scheme = getSchemeById(id);
        scheme.setIsActive(false);
        schemeRepository.save(scheme);
    }
}
