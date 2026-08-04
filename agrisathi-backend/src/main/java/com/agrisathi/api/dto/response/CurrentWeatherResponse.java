package com.agrisathi.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWeatherResponse {
    private Double latitude;
    private Double longitude;
    private Double temperature;
    private Double apparentTemperature;
    private Integer humidity;
    private Double windSpeed;
    private Double windDirection;
    private Double pressure;
    private Double precipitation;
    private String weatherCondition;
    private Integer weatherCode;
    private Double uvIndex;
    private String forecast;
    private String timestamp;
}
