package com.agrisathi.api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_cache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double temperature;

    private Double apparentTemperature;

    @Column(nullable = false)
    private Integer humidity;

    @Column(name = "wind_speed", nullable = false)
    private Double windSpeed;

    private Double windDirection;
    private Double pressure;
    private Double precipitation;

    @Column(length = 255)
    private String weatherCondition;

    private Integer weatherCode;
    private Double uvIndex;

    @Column(length = 255)
    private String forecast;

    @Column(columnDefinition = "TEXT")
    private String rawResponseJson;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
