package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.CropRequest;
import com.agrisathi.api.dto.response.CropResponse;
import com.agrisathi.api.exception.ResourceNotFoundException;
import com.agrisathi.api.model.entity.Crop;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.model.enums.CropStatus;
import com.agrisathi.api.repository.CropRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CropResponse> getCropsByUserId(Long userId) {
        return cropRepository.findByUserId(userId).stream()
                .map(CropResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CropResponse getCropByIdAndUserId(Long cropId, Long userId) {
        Crop crop = cropRepository.findByIdAndUserId(cropId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + cropId));
        return CropResponse.fromEntity(crop);
    }

    @Override
    @Transactional
    public CropResponse addCrop(Long userId, CropRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        CropStatus initialStatus = request.getStatus() != null ? request.getStatus() : CropStatus.PLANTED;

        Crop crop = Crop.builder()
                .user(user)
                .cropName(request.getCropName())
                .sowingDate(request.getSowingDate())
                .harvestDate(request.getHarvestDate())
                .status(initialStatus)
                .build();

        Crop savedCrop = cropRepository.save(crop);
        return CropResponse.fromEntity(savedCrop);
    }

    @Override
    @Transactional
    public CropResponse updateCrop(Long cropId, Long userId, CropRequest request) {
        Crop crop = cropRepository.findByIdAndUserId(cropId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + cropId));

        crop.setCropName(request.getCropName());
        crop.setSowingDate(request.getSowingDate());
        if (request.getHarvestDate() != null) {
            crop.setHarvestDate(request.getHarvestDate());
        }
        if (request.getStatus() != null) {
            crop.setStatus(request.getStatus());
        }
        Crop updatedCrop = cropRepository.save(crop);
        return CropResponse.fromEntity(updatedCrop);
    }

    @Override
    @Transactional
    public void deleteCrop(Long cropId, Long userId) {
        Crop crop = cropRepository.findByIdAndUserId(cropId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + cropId));
        cropRepository.delete(crop);
    }
}
