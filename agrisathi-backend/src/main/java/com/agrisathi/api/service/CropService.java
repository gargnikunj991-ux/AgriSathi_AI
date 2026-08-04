package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.CropRequest;
import com.agrisathi.api.dto.response.CropResponse;

import java.util.List;

public interface CropService {
    List<CropResponse> getCropsByUserId(Long userId);
    CropResponse getCropByIdAndUserId(Long cropId, Long userId);
    CropResponse addCrop(Long userId, CropRequest request);
    CropResponse updateCrop(Long cropId, Long userId, CropRequest request);
    void deleteCrop(Long cropId, Long userId);
}

