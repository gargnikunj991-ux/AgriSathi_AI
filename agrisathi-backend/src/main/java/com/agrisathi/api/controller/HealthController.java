package com.agrisathi.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Public Health Check Controller for Render and external container health probes.
 */
@RestController
public class HealthController {

    @GetMapping(value = {"/", "/api/v1/health"})
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "agrisathi-backend");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }
}
