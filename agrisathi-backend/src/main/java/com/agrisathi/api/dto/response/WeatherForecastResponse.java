package com.agrisathi.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherForecastResponse {
    private Double latitude;
    private Double longitude;
    private String timezone;
    private Integer days;
    private List<DailyForecast> dailyForecasts;
}
