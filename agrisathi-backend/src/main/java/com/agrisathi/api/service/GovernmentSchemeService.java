package com.agrisathi.api.service;

import com.agrisathi.api.model.entity.GovernmentScheme;

import java.util.List;

public interface GovernmentSchemeService {
    List<GovernmentScheme> getSchemes(String state);
}
