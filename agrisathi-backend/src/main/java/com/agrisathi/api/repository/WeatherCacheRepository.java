package com.agrisathi.api.repository;

import com.agrisathi.api.model.entity.WeatherCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WeatherCacheRepository extends JpaRepository<WeatherCache, Long> {

    @Query("SELECT w FROM WeatherCache w WHERE ABS(w.latitude - :lat) < 0.05 AND ABS(w.longitude - :lon) < 0.05 AND w.updatedAt > :cutoff ORDER BY w.updatedAt DESC LIMIT 1")
    Optional<WeatherCache> findCachedWeather(
            @Param("lat") Double lat,
            @Param("lon") Double lon,
            @Param("cutoff") LocalDateTime cutoff);
}
