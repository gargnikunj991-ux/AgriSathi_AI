package com.agrisathi.api.service.impl;

import com.agrisathi.api.model.entity.GovernmentScheme;
import com.agrisathi.api.repository.GovernmentSchemeRepository;
import com.agrisathi.api.service.GovernmentSchemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GovernmentSchemeServiceImpl implements GovernmentSchemeService {

    private final GovernmentSchemeRepository schemeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GovernmentScheme> getSchemes(String state) {
        if (StringUtils.hasText(state)) {
            return schemeRepository.findByStateIgnoreCaseAndIsActiveTrue(state);
        }
        return schemeRepository.findByIsActiveTrue();
    }
}
