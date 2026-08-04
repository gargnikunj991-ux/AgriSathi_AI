package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.*;
import com.agrisathi.api.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
@Tag(name = "Weather APIs", description = "Endpoints for hyper-local weather conditions, 7-day agromet forecasts, and farming decision advisories")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    @Operation(summary = "Get General Weather Overview", description = "Fetches basic current weather overview for latitude and longitude coordinates.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Weather forecast retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<WeatherResponse>> getWeather(
            @Parameter(description = "Latitude coordinate", example = "30.3165") @RequestParam(defaultValue = "30.3165") double lat,
            @Parameter(description = "Longitude coordinate", example = "78.0322") @RequestParam(defaultValue = "78.0322") double lon) {
        WeatherResponse weather = weatherService.getWeather(lat, lon);
        return ResponseEntity.ok(ApiResponse.success("Weather forecast retrieved successfully", weather));
    }

    @GetMapping("/current")
    @Operation(summary = "Get Detailed Current Weather", description = "Fetches detailed real-time meteorological metrics including temperature, humidity, wind, pressure, precipitation, and UV index.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Current weather retrieved successfully")
    })
    public ResponseEntity<ApiResponse<CurrentWeatherResponse>> getCurrentWeather(
            @Parameter(description = "Latitude coordinate", example = "30.3165") @RequestParam(defaultValue = "30.3165") double lat,
            @Parameter(description = "Longitude coordinate", example = "78.0322") @RequestParam(defaultValue = "78.0322") double lon) {
        CurrentWeatherResponse weather = weatherService.getCurrentWeather(lat, lon);
        return ResponseEntity.ok(ApiResponse.success("Current weather retrieved successfully", weather));
    }

    @GetMapping("/forecast")
    @Operation(summary = "Get Multi-Day Weather Forecast", description = "Fetches multi-day daily weather forecast (default 7 days) with min/max temperatures, precipitation probability, and wind speeds.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Weather forecast retrieved successfully")
    })
    public ResponseEntity<ApiResponse<WeatherForecastResponse>> getForecast(
            @Parameter(description = "Latitude coordinate", example = "30.3165") @RequestParam(defaultValue = "30.3165") double lat,
            @Parameter(description = "Longitude coordinate", example = "78.0322") @RequestParam(defaultValue = "78.0322") double lon,
            @Parameter(description = "Forecast duration in days (1 to 16)", example = "7") @RequestParam(defaultValue = "7") int days) {
        WeatherForecastResponse forecast = weatherService.getForecast(lat, lon, days);
        return ResponseEntity.ok(ApiResponse.success("Weather forecast retrieved successfully", forecast));
    }

    @GetMapping("/farming-summary")
    @Operation(summary = "Get Farming Weather Summary & Agromet Advisory", description = "Generates actionable farming advisories for irrigation, pesticide spraying safety, frost/heat stress risk, and sowing/harvest suitability.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farming weather summary retrieved successfully")
    })
    public ResponseEntity<ApiResponse<FarmingWeatherSummaryResponse>> getFarmingSummary(
            @Parameter(description = "Latitude coordinate", example = "30.3165") @RequestParam(defaultValue = "30.3165") double lat,
            @Parameter(description = "Longitude coordinate", example = "78.0322") @RequestParam(defaultValue = "78.0322") double lon) {
        FarmingWeatherSummaryResponse summary = weatherService.getFarmingSummary(lat, lon);
        return ResponseEntity.ok(ApiResponse.success("Farming weather summary retrieved successfully", summary));
    }
}
