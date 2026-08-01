package com.agrisathi.api.service;

import com.agrisathi.api.dto.response.WeatherResponse;

public interface WeatherService {
    WeatherResponse getWeather(double lat, double lon);
}
