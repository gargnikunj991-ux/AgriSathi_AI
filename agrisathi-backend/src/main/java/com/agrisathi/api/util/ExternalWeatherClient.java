package com.agrisathi.api.util;

import com.agrisathi.api.dto.response.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ExternalWeatherClient {

    private final RestTemplate restTemplate;

    public WeatherResponse fetchCurrentWeather(double lat, double lon) {
        // Fallback / standard weather response structure
        return WeatherResponse.builder()
                .temperature(31.0)
                .humidity(64)
                .windSpeed(11.0)
                .forecast("Rain Expected")
                .build();
    }
}
