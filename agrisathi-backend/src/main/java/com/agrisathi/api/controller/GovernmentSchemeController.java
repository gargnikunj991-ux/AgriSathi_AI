package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.model.entity.GovernmentScheme;
import com.agrisathi.api.service.GovernmentSchemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/government-schemes")
@RequiredArgsConstructor
public class GovernmentSchemeController {

    private final GovernmentSchemeService schemeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GovernmentScheme>>> getSchemes(
            @RequestParam(required = false) String state) {
        List<GovernmentScheme> schemes = schemeService.getSchemes(state);
        return ResponseEntity.ok(ApiResponse.success("Government schemes retrieved successfully", schemes));
    }
}
