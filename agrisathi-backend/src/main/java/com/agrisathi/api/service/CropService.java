package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.CropRequest;
import com.agrisathi.api.model.entity.Crop;

import java.util.List;

public interface CropService {
    List<Crop> getCropsByUserId(Long userId);
    Crop getCropByIdAndUserId(Long cropId, Long userId);
    Crop addCrop(Long userId, CropRequest request);
    Crop updateCrop(Long cropId, Long userId, CropRequest request);
    void deleteCrop(Long cropId, Long userId);
}
