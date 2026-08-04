package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.response.*;
import com.agrisathi.api.model.entity.WeatherCache;
import com.agrisathi.api.repository.WeatherCacheRepository;
import com.agrisathi.api.service.WeatherService;
import com.agrisathi.api.util.ExternalWeatherClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final ExternalWeatherClient weatherClient;
    private final WeatherCacheRepository weatherCacheRepository;

    @Override
    public WeatherResponse getWeather(double lat, double lon) {
        CurrentWeatherResponse current = getCurrentWeather(lat, lon);
        return WeatherResponse.builder()
                .temperature(current.getTemperature())
                .humidity(current.getHumidity())
                .windSpeed(current.getWindSpeed())
                .forecast(current.getForecast())
                .build();
    }

    @Override
    public CurrentWeatherResponse getCurrentWeather(double lat, double lon) {
        log.info("[WEATHER_SERVICE_CURRENT] Weather requested for coordinates ({}, {})", lat, lon);
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(30);
        Optional<WeatherCache> cachedOpt = weatherCacheRepository.findCachedWeather(lat, lon, cutoff);

        if (cachedOpt.isPresent()) {
            WeatherCache cache = cachedOpt.get();
            log.info("[WEATHER_CACHE_HIT] Returning cached weather data for coordinates ({}, {})", lat, lon);
            return CurrentWeatherResponse.builder()
                    .latitude(cache.getLatitude())
                    .longitude(cache.getLongitude())
                    .temperature(cache.getTemperature())
                    .apparentTemperature(cache.getApparentTemperature() != null ? cache.getApparentTemperature() : cache.getTemperature())
                    .humidity(cache.getHumidity())
                    .windSpeed(cache.getWindSpeed())
                    .windDirection(cache.getWindDirection() != null ? cache.getWindDirection() : 180.0)
                    .pressure(cache.getPressure() != null ? cache.getPressure() : 1013.25)
                    .precipitation(cache.getPrecipitation() != null ? cache.getPrecipitation() : 0.0)
                    .weatherCondition(cache.getWeatherCondition() != null ? cache.getWeatherCondition() : "Clear sky")
                    .weatherCode(cache.getWeatherCode() != null ? cache.getWeatherCode() : 0)
                    .uvIndex(cache.getUvIndex() != null ? cache.getUvIndex() : 5.0)
                    .forecast(cache.getForecast())
                    .timestamp(cache.getUpdatedAt().toString())
                    .build();
        }

        CurrentWeatherResponse fetched = weatherClient.fetchCurrentWeather(lat, lon);
        saveToCache(fetched);
        return fetched;
    }

    @Override
    public WeatherForecastResponse getForecast(double lat, double lon, int days) {
        log.info("[WEATHER_SERVICE_FORECAST] Multi-day weather forecast requested for ({}, {}), days: {}", lat, lon, days);
        return weatherClient.fetchForecast(lat, lon, days);
    }

    @Override
    public FarmingWeatherSummaryResponse getFarmingSummary(double lat, double lon) {
        log.info("[WEATHER_SERVICE_SUMMARY] Farming weather summary & agromet advisory requested for ({}, {})", lat, lon);
        CurrentWeatherResponse current = getCurrentWeather(lat, lon);
        WeatherForecastResponse forecast = getForecast(lat, lon, 7);

        double temp = current.getTemperature() != null ? current.getTemperature() : 25.0;
        int humidity = current.getHumidity() != null ? current.getHumidity() : 60;
        double windSpeed = current.getWindSpeed() != null ? current.getWindSpeed() : 10.0;
        double prec = current.getPrecipitation() != null ? current.getPrecipitation() : 0.0;

        // Calculate 7-day forecast aggregates
        double totalForecastRain = 0.0;
        double minTempWeek = temp;
        double maxTempWeek = temp;
        boolean heavyRainExpected = false;

        if (forecast.getDailyForecasts() != null) {
            for (DailyForecast day : forecast.getDailyForecasts()) {
                if (day.getPrecipitation() != null) totalForecastRain += day.getPrecipitation();
                if (day.getTempMin() != null && day.getTempMin() < minTempWeek) minTempWeek = day.getTempMin();
                if (day.getTempMax() != null && day.getTempMax() > maxTempWeek) maxTempWeek = day.getTempMax();
                if (day.getPrecipitationProbability() != null && day.getPrecipitationProbability() > 70) {
                    heavyRainExpected = true;
                }
            }
        }

        // Irrigation Advice
        String irrigationAdvice;
        if (prec > 2.0 || totalForecastRain > 10.0) {
            irrigationAdvice = "Hold irrigation: Expected rainfall (" + String.format("%.1f", totalForecastRain) + " mm) will maintain adequate soil moisture.";
        } else if (humidity < 40 && temp > 32.0) {
            irrigationAdvice = "Irrigate promptly: High evapotranspiration risk due to elevated temperatures and low humidity.";
        } else {
            irrigationAdvice = "Standard irrigation schedule: Maintain normal watering routines for standing crops.";
        }

        // Spraying Conditions
        String sprayingCondition;
        if (windSpeed > 18.0) {
            sprayingCondition = "Unfavorable: Wind speed (" + String.format("%.1f", windSpeed) + " km/h) is too high for pesticide/herbicide application.";
        } else if (prec > 0.5 || heavyRainExpected) {
            sprayingCondition = "Unfavorable: Imminent rain may wash off chemical applications.";
        } else {
            sprayingCondition = "Favorable: Moderate winds and dry atmosphere provide safe spraying conditions.";
        }

        // Frost Risk
        String frostRisk;
        if (minTempWeek < 4.0) {
            frostRisk = "High Risk: Low temperature (" + String.format("%.1f", minTempWeek) + "°C) may cause frost damage. Cover vulnerable seedlings.";
        } else if (minTempWeek < 10.0) {
            frostRisk = "Moderate Risk: Cool night temperatures detected (" + String.format("%.1f", minTempWeek) + "°C). Monitor crop sensitivity.";
        } else {
            frostRisk = "Low Risk: Temperatures will remain safely above freezing threshold.";
        }

        // Heat Stress Risk
        String heatStressRisk;
        if (maxTempWeek > 40.0) {
            heatStressRisk = "High Risk: Temperatures above 40°C. Increase irrigation frequency and apply mulching to protect roots.";
        } else if (maxTempWeek > 35.0) {
            heatStressRisk = "Moderate Risk: Warm conditions (" + String.format("%.1f", maxTempWeek) + "°C). Ensure adequate crop hydration.";
        } else {
            heatStressRisk = "Low Risk: Temperatures are within healthy crop growth thresholds.";
        }

        // Sowing & Harvesting Suitability
        String sowingSuitability = (prec > 20.0 || maxTempWeek > 42.0) ?
                "Unfavorable: Heavy rains or extreme heat may impair seed germination." :
                "Optimal: Soil temperature and moisture conditions support healthy seed germination.";

        String harvestingSuitability = (heavyRainExpected || totalForecastRain > 15.0) ?
                "Avoid Harvesting: Rainfall forecast might cause post-harvest crop spoilage or wet field access issues." :
                "Optimal: Dry weather conditions facilitate efficient crop harvesting and drying.";

        // Overall Advisory
        String overallAdvisory = String.format(
                "Current conditions: %.1f°C, %d%% humidity, %.1f km/h wind. 7-day expected precipitation: %.1f mm.",
                temp, humidity, windSpeed, totalForecastRain);

        // Action items list
        List<String> actions = new ArrayList<>();
        if (totalForecastRain > 10.0) {
            actions.add("Ensure farm drainage channels are clear to prevent waterlogging.");
        }
        if (sprayingCondition.startsWith("Favorable")) {
            actions.add("Good window for preventive pesticide/nutrient foliar spray.");
        }
        if (irrigationAdvice.startsWith("Irrigate")) {
            actions.add("Plan early morning or evening irrigation to minimize evaporation loss.");
        }
        if (actions.isEmpty()) {
            actions.add("Monitor crops regularly and follow standard seasonal field practices.");
        }

        return FarmingWeatherSummaryResponse.builder()
                .latitude(lat)
                .longitude(lon)
                .currentWeather(current)
                .overallAdvisory(overallAdvisory)
                .irrigationAdvice(irrigationAdvice)
                .sprayingCondition(sprayingCondition)
                .frostRisk(frostRisk)
                .heatStressRisk(heatStressRisk)
                .sowingSuitability(sowingSuitability)
                .harvestingSuitability(harvestingSuitability)
                .actionItems(actions)
                .build();
    }

    private void saveToCache(CurrentWeatherResponse response) {
        try {
            WeatherCache cache = WeatherCache.builder()
                    .latitude(response.getLatitude())
                    .longitude(response.getLongitude())
                    .temperature(response.getTemperature())
                    .apparentTemperature(response.getApparentTemperature())
                    .humidity(response.getHumidity())
                    .windSpeed(response.getWindSpeed())
                    .windDirection(response.getWindDirection())
                    .pressure(response.getPressure())
                    .precipitation(response.getPrecipitation())
                    .weatherCondition(response.getWeatherCondition())
                    .weatherCode(response.getWeatherCode())
                    .uvIndex(response.getUvIndex())
                    .forecast(response.getForecast())
                    .build();

            weatherCacheRepository.save(cache);
        } catch (Exception e) {
            log.warn("Could not save weather data to cache: {}", e.getMessage());
        }
    }
}
