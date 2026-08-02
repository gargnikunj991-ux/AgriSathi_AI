package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.*;
import com.agrisathi.api.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Legacy / general weather overview endpoint
     */
    @GetMapping
    public ResponseEntity<ApiResponse<WeatherResponse>> getWeather(
            @RequestParam(defaultValue = "30.3165") double lat,
            @RequestParam(defaultValue = "78.0322") double lon) {
        WeatherResponse weather = weatherService.getWeather(lat, lon);
        return ResponseEntity.ok(ApiResponse.success("Weather forecast retrieved successfully", weather));
    }

    /**
     * Current Weather Endpoint
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<CurrentWeatherResponse>> getCurrentWeather(
            @RequestParam(defaultValue = "30.3165") double lat,
            @RequestParam(defaultValue = "78.0322") double lon) {
        CurrentWeatherResponse weather = weatherService.getCurrentWeather(lat, lon);
        return ResponseEntity.ok(ApiResponse.success("Current weather retrieved successfully", weather));
    }

    /**
     * Weather Forecast Endpoint (e.g. 7-day daily forecast)
     */
    @GetMapping("/forecast")
    public ResponseEntity<ApiResponse<WeatherForecastResponse>> getForecast(
            @RequestParam(defaultValue = "30.3165") double lat,
            @RequestParam(defaultValue = "78.0322") double lon,
            @RequestParam(defaultValue = "7") int days) {
        WeatherForecastResponse forecast = weatherService.getForecast(lat, lon, days);
        return ResponseEntity.ok(ApiResponse.success("Weather forecast retrieved successfully", forecast));
    }

    /**
     * Farming Weather Summary & Agricultural Advisory Endpoint
     */
    @GetMapping("/farming-summary")
    public ResponseEntity<ApiResponse<FarmingWeatherSummaryResponse>> getFarmingSummary(
            @RequestParam(defaultValue = "30.3165") double lat,
            @RequestParam(defaultValue = "78.0322") double lon) {
        FarmingWeatherSummaryResponse summary = weatherService.getFarmingSummary(lat, lon);
        return ResponseEntity.ok(ApiResponse.success("Farming weather summary retrieved successfully", summary));
    }
}
