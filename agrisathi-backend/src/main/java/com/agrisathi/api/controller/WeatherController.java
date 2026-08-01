package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.WeatherResponse;
import com.agrisathi.api.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<ApiResponse<WeatherResponse>> getWeather(
            @RequestParam(defaultValue = "30.3165") double lat,
            @RequestParam(defaultValue = "78.0322") double lon) {
        WeatherResponse weather = weatherService.getWeather(lat, lon);
        return ResponseEntity.ok(ApiResponse.success("Weather forecast retrieved successfully", weather));
    }
}
