package com.agrisathi.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyForecast {
    private String date;
    private Double tempMin;
    private Double tempMax;
    private Integer precipitationProbability;
    private Double precipitation;
    private Double windSpeedMax;
    private String weatherCondition;
    private Integer weatherCode;
    private Double uvIndexMax;
}
