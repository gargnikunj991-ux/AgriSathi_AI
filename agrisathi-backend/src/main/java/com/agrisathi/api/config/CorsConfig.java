package com.agrisathi.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cross-Origin Resource Sharing (CORS) security configuration.
 */
@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:http://localhost:3000,http://localhost:5173,http://localhost:8080,https://*.vercel.app,https://*.onrender.com,https://*.netlify.app}")
    private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        List<String> originPatterns = new ArrayList<>();
        if (allowedOrigins != null && !allowedOrigins.isBlank()) {
            originPatterns = Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }
        if (originPatterns.isEmpty()) {
            originPatterns = List.of(
                    "http://localhost:3000",
                    "http://localhost:5173",
                    "http://localhost:8080",
                    "https://*.vercel.app",
                    "https://*.onrender.com",
                    "https://*.netlify.app"
            );
        }

        // Use setAllowedOriginPatterns instead of setAllowedOrigins to allow wildcards with allowCredentials(true)
        configuration.setAllowedOriginPatterns(originPatterns);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Link", "X-Total-Count", "Content-Disposition"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

