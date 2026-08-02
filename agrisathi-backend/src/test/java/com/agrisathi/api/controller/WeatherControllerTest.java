package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.CurrentWeatherResponse;
import com.agrisathi.api.dto.response.DailyForecast;
import com.agrisathi.api.dto.response.FarmingWeatherSummaryResponse;
import com.agrisathi.api.dto.response.WeatherForecastResponse;
import com.agrisathi.api.dto.response.WeatherResponse;
import com.agrisathi.api.security.JwtAuthenticationEntryPoint;
import com.agrisathi.api.security.JwtTokenProvider;
import com.agrisathi.api.security.UserDetailsServiceImpl;
import com.agrisathi.api.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
@AutoConfigureMockMvc(addFilters = false)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @DisplayName("GET /api/v1/weather - Should return legacy weather overview")
    void testGetWeatherOverview() throws Exception {
        WeatherResponse response = WeatherResponse.builder()
                .temperature(30.5)
                .humidity(65)
                .windSpeed(12.0)
                .forecast("Partly Cloudy")
                .build();

        given(weatherService.getWeather(anyDouble(), anyDouble())).willReturn(response);

        mockMvc.perform(get("/api/v1/weather")
                        .param("lat", "30.3165")
                        .param("lon", "78.0322")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.temperature").value(30.5))
                .andExpect(jsonPath("$.data.humidity").value(65))
                .andExpect(jsonPath("$.data.windSpeed").value(12.0))
                .andExpect(jsonPath("$.data.forecast").value("Partly Cloudy"));
    }

    @Test
    @DisplayName("GET /api/v1/weather/current - Should return detailed current weather")
    void testGetCurrentWeather() throws Exception {
        CurrentWeatherResponse current = CurrentWeatherResponse.builder()
                .latitude(30.3165)
                .longitude(78.0322)
                .temperature(31.0)
                .apparentTemperature(33.5)
                .humidity(64)
                .windSpeed(11.0)
                .windDirection(180.0)
                .pressure(1013.25)
                .precipitation(0.0)
                .weatherCondition("Clear sky")
                .weatherCode(0)
                .uvIndex(6.0)
                .forecast("Clear & Sunny")
                .timestamp("2026-08-02T14:30:00")
                .build();

        given(weatherService.getCurrentWeather(anyDouble(), anyDouble())).willReturn(current);

        mockMvc.perform(get("/api/v1/weather/current")
                        .param("lat", "30.3165")
                        .param("lon", "78.0322"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.latitude").value(30.3165))
                .andExpect(jsonPath("$.data.temperature").value(31.0))
                .andExpect(jsonPath("$.data.apparentTemperature").value(33.5))
                .andExpect(jsonPath("$.data.weatherCondition").value("Clear sky"));
    }

    @Test
    @DisplayName("GET /api/v1/weather/forecast - Should return daily weather forecast")
    void testGetForecast() throws Exception {
        DailyForecast day1 = DailyForecast.builder()
                .date("2026-08-02")
                .tempMin(22.0)
                .tempMax(33.0)
                .precipitationProbability(20)
                .precipitation(0.0)
                .windSpeedMax(14.0)
                .weatherCondition("Partly cloudy")
                .weatherCode(2)
                .uvIndexMax(7.0)
                .build();

        WeatherForecastResponse forecast = WeatherForecastResponse.builder()
                .latitude(30.3165)
                .longitude(78.0322)
                .timezone("Asia/Kolkata")
                .days(1)
                .dailyForecasts(List.of(day1))
                .build();

        given(weatherService.getForecast(anyDouble(), anyDouble(), anyInt())).willReturn(forecast);

        mockMvc.perform(get("/api/v1/weather/forecast")
                        .param("lat", "30.3165")
                        .param("lon", "78.0322")
                        .param("days", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.dailyForecasts[0].date").value("2026-08-02"))
                .andExpect(jsonPath("$.data.dailyForecasts[0].tempMax").value(33.0));
    }

    @Test
    @DisplayName("GET /api/v1/weather/farming-summary - Should return agricultural weather advisory")
    void testGetFarmingSummary() throws Exception {
        CurrentWeatherResponse current = CurrentWeatherResponse.builder()
                .temperature(29.0)
                .humidity(70)
                .windSpeed(10.0)
                .precipitation(0.0)
                .weatherCondition("Partly cloudy")
                .build();

        FarmingWeatherSummaryResponse summary = FarmingWeatherSummaryResponse.builder()
                .latitude(30.3165)
                .longitude(78.0322)
                .currentWeather(current)
                .overallAdvisory("Favorable conditions for field operations")
                .irrigationAdvice("Standard irrigation schedule")
                .sprayingCondition("Favorable: Moderate winds")
                .frostRisk("Low Risk")
                .heatStressRisk("Low Risk")
                .sowingSuitability("Optimal")
                .harvestingSuitability("Optimal")
                .actionItems(List.of("Monitor crop health regularly"))
                .build();

        given(weatherService.getFarmingSummary(anyDouble(), anyDouble())).willReturn(summary);

        mockMvc.perform(get("/api/v1/weather/farming-summary")
                        .param("lat", "30.3165")
                        .param("lon", "78.0322"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.overallAdvisory").value("Favorable conditions for field operations"))
                .andExpect(jsonPath("$.data.sprayingCondition").value("Favorable: Moderate winds"))
                .andExpect(jsonPath("$.data.actionItems[0]").value("Monitor crop health regularly"));
    }
}
