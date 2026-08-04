package com.agrisathi.api.service;

import com.agrisathi.api.dto.response.CurrentWeatherResponse;
import com.agrisathi.api.dto.response.DailyForecast;
import com.agrisathi.api.dto.response.FarmingWeatherSummaryResponse;
import com.agrisathi.api.dto.response.WeatherForecastResponse;
import com.agrisathi.api.model.entity.WeatherCache;
import com.agrisathi.api.repository.WeatherCacheRepository;
import com.agrisathi.api.service.impl.WeatherServiceImpl;
import com.agrisathi.api.util.ExternalWeatherClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private ExternalWeatherClient externalWeatherClient;

    @Mock
    private WeatherCacheRepository weatherCacheRepository;

    private WeatherServiceImpl weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherServiceImpl(externalWeatherClient, weatherCacheRepository);
    }

    @Test
    @DisplayName("getCurrentWeather - Should return cached weather if available and valid")
    void testGetCurrentWeatherCached() {
        WeatherCache cached = WeatherCache.builder()
                .latitude(30.3165)
                .longitude(78.0322)
                .temperature(28.0)
                .apparentTemperature(30.0)
                .humidity(65)
                .windSpeed(10.0)
                .windDirection(180.0)
                .pressure(1012.0)
                .precipitation(0.0)
                .weatherCondition("Partly cloudy")
                .weatherCode(2)
                .uvIndex(5.0)
                .forecast("Partly cloudy with pleasant breeze")
                .updatedAt(LocalDateTime.now())
                .build();

        given(weatherCacheRepository.findCachedWeather(anyDouble(), anyDouble(), any())).willReturn(Optional.of(cached));

        CurrentWeatherResponse response = weatherService.getCurrentWeather(30.3165, 78.0322);

        assertThat(response).isNotNull();
        assertThat(response.getTemperature()).isEqualTo(28.0);
        assertThat(response.getWeatherCondition()).isEqualTo("Partly cloudy");
    }

    @Test
    @DisplayName("getCurrentWeather - Should fetch from external client if cache missing")
    void testGetCurrentWeatherUncached() {
        CurrentWeatherResponse fetched = CurrentWeatherResponse.builder()
                .latitude(30.3165)
                .longitude(78.0322)
                .temperature(32.0)
                .humidity(55)
                .windSpeed(14.0)
                .weatherCondition("Clear sky")
                .weatherCode(0)
                .forecast("Clear & Sunny")
                .build();

        given(weatherCacheRepository.findCachedWeather(anyDouble(), anyDouble(), any())).willReturn(Optional.empty());
        given(externalWeatherClient.fetchCurrentWeather(30.3165, 78.0322)).willReturn(fetched);

        CurrentWeatherResponse response = weatherService.getCurrentWeather(30.3165, 78.0322);

        assertThat(response).isNotNull();
        assertThat(response.getTemperature()).isEqualTo(32.0);
        verify(externalWeatherClient).fetchCurrentWeather(30.3165, 78.0322);
    }

    @Test
    @DisplayName("getFarmingSummary - Should compute agricultural advisory correctly")
    void testGetFarmingSummary() {
        CurrentWeatherResponse current = CurrentWeatherResponse.builder()
                .latitude(30.3165)
                .longitude(78.0322)
                .temperature(30.0)
                .humidity(60)
                .windSpeed(12.0)
                .precipitation(0.0)
                .weatherCondition("Clear sky")
                .build();

        DailyForecast forecastDay = DailyForecast.builder()
                .date("2026-08-02")
                .tempMin(22.0)
                .tempMax(34.0)
                .precipitationProbability(10)
                .precipitation(0.0)
                .windSpeedMax(15.0)
                .weatherCondition("Clear sky")
                .build();

        WeatherForecastResponse forecast = WeatherForecastResponse.builder()
                .latitude(30.3165)
                .longitude(78.0322)
                .dailyForecasts(List.of(forecastDay))
                .build();

        given(weatherCacheRepository.findCachedWeather(anyDouble(), anyDouble(), any())).willReturn(Optional.empty());
        given(externalWeatherClient.fetchCurrentWeather(anyDouble(), anyDouble())).willReturn(current);
        given(externalWeatherClient.fetchForecast(anyDouble(), anyDouble(), anyInt())).willReturn(forecast);

        FarmingWeatherSummaryResponse summary = weatherService.getFarmingSummary(30.3165, 78.0322);

        assertThat(summary).isNotNull();
        assertThat(summary.getSprayingCondition()).contains("Favorable");
        assertThat(summary.getFrostRisk()).contains("Low Risk");
        assertThat(summary.getHeatStressRisk()).contains("Low Risk");
        assertThat(summary.getSowingSuitability()).contains("Optimal");
    }
}
