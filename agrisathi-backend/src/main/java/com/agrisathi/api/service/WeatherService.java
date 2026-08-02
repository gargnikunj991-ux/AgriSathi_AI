package com.agrisathi.api.service;

import com.agrisathi.api.dto.response.CurrentWeatherResponse;
import com.agrisathi.api.dto.response.FarmingWeatherSummaryResponse;
import com.agrisathi.api.dto.response.WeatherForecastResponse;
import com.agrisathi.api.dto.response.WeatherResponse;

public interface WeatherService {
    WeatherResponse getWeather(double lat, double lon);
    CurrentWeatherResponse getCurrentWeather(double lat, double lon);
    WeatherForecastResponse getForecast(double lat, double lon, int days);
    FarmingWeatherSummaryResponse getFarmingSummary(double lat, double lon);
}
