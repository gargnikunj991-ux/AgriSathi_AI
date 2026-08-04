package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.CropAdviceRequest;
import com.agrisathi.api.dto.request.FertilizerRecommendationRequest;
import com.agrisathi.api.dto.response.CropAdviceResponse;
import com.agrisathi.api.dto.response.FertilizerRecommendationResponse;
import com.agrisathi.api.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Override
    public FertilizerRecommendationResponse recommendFertilizer(FertilizerRecommendationRequest request) {
        String crop = request.getCrop() != null ? request.getCrop().trim() : "Crop";
        String soilType = request.getSoilType() != null ? request.getSoilType().trim() : "Loamy";
        String disease = request.getDisease() != null ? request.getDisease().trim() : "";

        log.info("[AI_RECOMMENDATION_FERTILIZER_REQUEST] Fertilizer recommendation requested for crop: '{}', soilType: '{}', disease: '{}'",
                crop, soilType, disease);

        String fertilizer = "NPK 19:19:19 + Neem Cake";
        String quantity = "50 kg/acre";
        String applicationMethod = "Broadcast evenly near root zones before irrigation.";
        String applicationTiming = "Apply in 2 split doses: 50% at basal sowing and 50% at tillering stage.";
        List<String> organicAlternatives = Arrays.asList("Vermi-compost (2 tons/acre)", "Bio-fertilizers (Azotobacter & PSB)", "Farmyard Manure (FYM)");

        if (disease.equalsIgnoreCase("Leaf Rust") || disease.equalsIgnoreCase("Blight")) {
            fertilizer = "Copper Oxychloride 50% WP + NPK 12:61:0";
            quantity = "30 kg/acre (NPK) + 2.5g/L foliar spray (Fungicide)";
            applicationMethod = "Foliar spray during early morning or late evening.";
            applicationTiming = "Apply immediately upon symptom detection; repeat after 10-12 days.";
            organicAlternatives = Arrays.asList("Neem Oil Extract (5ml/L)", "Trichoderma viride bio-fungicide");
        } else if (soilType.equalsIgnoreCase("Sandy")) {
            fertilizer = "NPK 20:20:20 + Zinc Sulphate";
            quantity = "60 kg/acre";
            applicationMethod = "Band placement near crop rows.";
            applicationTiming = "Split into 3 doses to minimize leaching loss in sandy soil.";
            organicAlternatives = Arrays.asList("Organic Compost", "Green Manure (Dhaincha/Sunn hemp)");
        }

        log.info("[AI_RECOMMENDATION_FERTILIZER_SUCCESS] Recommended fertilizer: '{}', quantity: '{}' for crop '{}'",
                fertilizer, quantity, crop);

        return FertilizerRecommendationResponse.builder()
                .fertilizer(fertilizer)
                .quantity(quantity)
                .applicationMethod(applicationMethod)
                .applicationTiming(applicationTiming)
                .organicAlternatives(organicAlternatives)
                .build();
    }

    @Override
    public CropAdviceResponse recommendCropAdvice(CropAdviceRequest request) {
        String state = request.getState();
        String district = request.getDistrict();
        String soilType = request.getSoilType();
        String season = request.getSeason() != null ? request.getSeason() : "Kharif";

        log.info("[AI_RECOMMENDATION_CROP_REQUEST] Crop advice requested for state: '{}', district: '{}', soilType: '{}', season: '{}'",
                state, district, soilType, season);

        List<String> recommendedCrops;
        String optimalSowingWindow;
        String expectedYield;
        String estimatedProfit;
        String irrigationAdvice;
        String pestWarning;

        if (season.equalsIgnoreCase("Rabi")) {
            recommendedCrops = Arrays.asList("Wheat (HD-2967)", "Mustard (Pusa Bold)", "Chickpea (Desi Gram)", "Barley");
            optimalSowingWindow = "October 25 - November 20";
            expectedYield = "22 - 28 Quintals/acre";
            estimatedProfit = "₹45,000 - ₹60,000 per acre";
            irrigationAdvice = "Provide 4-5 critical irrigations (Crown Root Initiation, Flowering, Grain Filling).";
            pestWarning = "Monitor for Aphids and Pod Borer during flowering stage.";
        } else if (season.equalsIgnoreCase("Zaid")) {
            recommendedCrops = Arrays.asList("Watermelon", "Muskmelon", "Cucumber", "Moong Dal (Summer Pulse)");
            optimalSowingWindow = "March 1 - March 25";
            expectedYield = "80 - 120 Quintals/acre (Horticulture)";
            estimatedProfit = "₹55,000 - ₹75,000 per acre";
            irrigationAdvice = "Drip irrigation recommended every 3-4 days due to summer evapotranspiration.";
            pestWarning = "Watch out for Red Pumpkin Beetle and Fruit Fly.";
        } else { // Kharif default
            recommendedCrops = Arrays.asList("Paddy (Basmati / PR-126)", "Maize (Hybrid)", "Soybean", "Cotton");
            optimalSowingWindow = "June 15 - July 10";
            expectedYield = "25 - 32 Quintals/acre";
            estimatedProfit = "₹40,000 - ₹55,000 per acre";
            irrigationAdvice = "Maintain 2-3 cm standing water in paddy fields during initial 3 weeks.";
            pestWarning = "Preventive spray for Stem Borer and Brown Plant Hopper recommended.";
        }

        log.info("[AI_RECOMMENDATION_CROP_SUCCESS] Generated crop advice for '{}/{}' (season: {}): recommended crops = {}",
                district, state, season, recommendedCrops);

        return CropAdviceResponse.builder()
                .state(state)
                .district(district)
                .soilType(soilType)
                .season(season)
                .recommendedCrops(recommendedCrops)
                .optimalSowingWindow(optimalSowingWindow)
                .expectedYieldPerAcre(expectedYield)
                .estimatedProfitPerAcre(estimatedProfit)
                .irrigationAdvice(irrigationAdvice)
                .pestWarning(pestWarning)
                .build();
    }
}
