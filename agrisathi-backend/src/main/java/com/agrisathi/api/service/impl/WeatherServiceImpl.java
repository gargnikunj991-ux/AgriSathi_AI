package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.response.WeatherResponse;
import com.agrisathi.api.service.WeatherService;
import com.agrisathi.api.util.ExternalWeatherClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final ExternalWeatherClient weatherClient;

    @Override
    public WeatherResponse getWeather(double lat, double lon) {
        return weatherClient.fetchCurrentWeather(lat, lon);
    }
}
