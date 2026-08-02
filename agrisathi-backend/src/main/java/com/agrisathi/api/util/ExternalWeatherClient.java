package com.agrisathi.api.util;

import com.agrisathi.api.dto.response.CurrentWeatherResponse;
import com.agrisathi.api.dto.response.DailyForecast;
import com.agrisathi.api.dto.response.WeatherForecastResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalWeatherClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String OPEN_METEO_URL = "https://api.open-meteo.com/v1/forecast" +
            "?latitude={lat}&longitude={lon}" +
            "&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,surface_pressure,wind_speed_10m,wind_direction_10m" +
            "&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_probability_max,wind_speed_10m_max,uv_index_max" +
            "&timezone=auto";

    public CurrentWeatherResponse fetchCurrentWeather(double lat, double lon) {
        try {
            String responseStr = restTemplate.getForObject(OPEN_METEO_URL, String.class, lat, lon);
            if (responseStr != null) {
                JsonNode root = objectMapper.readTree(responseStr);
                if (root.has("current")) {
                    JsonNode current = root.get("current");
                    int code = current.path("weather_code").asInt(0);
                    String condition = mapWeatherCode(code);
                    double temp = current.path("temperature_2m").asDouble(28.0);
                    double apparentTemp = current.path("apparent_temperature").asDouble(temp);
                    int humidity = current.path("relative_humidity_2m").asInt(60);
                    double windSpeed = current.path("wind_speed_10m").asDouble(10.0);
                    double windDir = current.path("wind_direction_10m").asDouble(180.0);
                    double pressure = current.path("surface_pressure").asDouble(1013.25);
                    double prec = current.path("precipitation").asDouble(0.0);

                    return CurrentWeatherResponse.builder()
                            .latitude(lat)
                            .longitude(lon)
                            .temperature(temp)
                            .apparentTemperature(apparentTemp)
                            .humidity(humidity)
                            .windSpeed(windSpeed)
                            .windDirection(windDir)
                            .pressure(pressure)
                            .precipitation(prec)
                            .weatherCondition(condition)
                            .weatherCode(code)
                            .uvIndex(5.5)
                            .forecast(buildBriefForecastText(condition, prec, temp))
                            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                            .build();
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch live weather from Open-Meteo for ({}, {}): {}. Using fallback.", lat, lon, e.getMessage());
        }

        return buildFallbackCurrentWeather(lat, lon);
    }

    public WeatherForecastResponse fetchForecast(double lat, double lon, int days) {
        int requestedDays = Math.min(Math.max(days, 1), 14);
        try {
            String responseStr = restTemplate.getForObject(OPEN_METEO_URL, String.class, lat, lon);
            if (responseStr != null) {
                JsonNode root = objectMapper.readTree(responseStr);
                String timezone = root.path("timezone").asText("UTC");
                if (root.has("daily")) {
                    JsonNode daily = root.get("daily");
                    JsonNode times = daily.get("time");
                    JsonNode codes = daily.get("weather_code");
                    JsonNode maxTemps = daily.get("temperature_2m_max");
                    JsonNode minTemps = daily.get("temperature_2m_min");
                    JsonNode precSums = daily.get("precipitation_sum");
                    JsonNode precProbs = daily.get("precipitation_probability_max");
                    JsonNode windMaxs = daily.get("wind_speed_10m_max");
                    JsonNode uvMaxs = daily.get("uv_index_max");

                    List<DailyForecast> forecasts = new ArrayList<>();
                    int limit = Math.min(times.size(), requestedDays);

                    for (int i = 0; i < limit; i++) {
                        int code = codes.get(i).asInt(0);
                        forecasts.add(DailyForecast.builder()
                                .date(times.get(i).asText())
                                .tempMin(minTemps.get(i).asDouble(20.0))
                                .tempMax(maxTemps.get(i).asDouble(32.0))
                                .precipitationProbability(precProbs.has(i) ? precProbs.get(i).asInt(0) : 10)
                                .precipitation(precSums.get(i).asDouble(0.0))
                                .windSpeedMax(windMaxs.get(i).asDouble(12.0))
                                .weatherCondition(mapWeatherCode(code))
                                .weatherCode(code)
                                .uvIndexMax(uvMaxs.has(i) ? uvMaxs.get(i).asDouble(6.0) : 6.0)
                                .build());
                    }

                    return WeatherForecastResponse.builder()
                            .latitude(lat)
                            .longitude(lon)
                            .timezone(timezone)
                            .days(forecasts.size())
                            .dailyForecasts(forecasts)
                            .build();
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch live forecast from Open-Meteo for ({}, {}): {}. Using fallback.", lat, lon, e.getMessage());
        }

        return buildFallbackForecast(lat, lon, requestedDays);
    }

    private CurrentWeatherResponse buildFallbackCurrentWeather(double lat, double lon) {
        double temp = Math.round((28.0 + (lat % 5) - (lon % 3)) * 10.0) / 10.0;
        int humidity = (int) (60 + (lat % 20));
        double windSpeed = Math.round((10.0 + (lon % 8)) * 10.0) / 10.0;
        String condition = "Partly Cloudy";

        return CurrentWeatherResponse.builder()
                .latitude(lat)
                .longitude(lon)
                .temperature(temp)
                .apparentTemperature(temp + 2.0)
                .humidity(humidity)
                .windSpeed(windSpeed)
                .windDirection(180.0)
                .pressure(1012.0)
                .precipitation(0.0)
                .weatherCondition(condition)
                .weatherCode(2)
                .uvIndex(6.0)
                .forecast("Partly cloudy with pleasant breeze")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    private WeatherForecastResponse buildFallbackForecast(double lat, double lon, int days) {
        List<DailyForecast> forecasts = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < days; i++) {
            LocalDate date = today.plusDays(i);
            int code = (i % 3 == 0) ? 61 : (i % 2 == 0) ? 2 : 0;
            String condition = mapWeatherCode(code);

            forecasts.add(DailyForecast.builder()
                    .date(date.toString())
                    .tempMin(Math.round((20.0 + i) * 10.0) / 10.0)
                    .tempMax(Math.round((32.0 + (i % 2)) * 10.0) / 10.0)
                    .precipitationProbability(code == 61 ? 70 : 15)
                    .precipitation(code == 61 ? 12.5 : 0.0)
                    .windSpeedMax(14.0)
                    .weatherCondition(condition)
                    .weatherCode(code)
                    .uvIndexMax(6.5)
                    .build());
        }

        return WeatherForecastResponse.builder()
                .latitude(lat)
                .longitude(lon)
                .timezone("Asia/Kolkata")
                .days(days)
                .dailyForecasts(forecasts)
                .build();
    }

    public String mapWeatherCode(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1 -> "Mainly clear";
            case 2 -> "Partly cloudy";
            case 3 -> "Overcast";
            case 45, 48 -> "Foggy";
            case 51, 53, 55 -> "Drizzle";
            case 56, 57 -> "Freezing Drizzle";
            case 61, 63, 65 -> "Rain";
            case 66, 67 -> "Freezing Rain";
            case 71, 73, 75, 77 -> "Snowfall";
            case 80, 81, 82 -> "Rain Showers";
            case 85, 86 -> "Snow Showers";
            case 95, 96, 99 -> "Thunderstorm";
            default -> "Clear sky";
        };
    }

    private String buildBriefForecastText(String condition, double prec, double temp) {
        if (prec > 5.0 || condition.toLowerCase().contains("rain") || condition.toLowerCase().contains("thunderstorm")) {
            return "Rain Expected";
        } else if (temp > 35.0) {
            return "Hot & Dry";
        } else if (condition.toLowerCase().contains("cloud")) {
            return "Partly Cloudy";
        } else {
            return "Clear & Sunny";
        }
    }
}
