package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.CropRequest;
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
    public List<Crop> getCropsByUserId(Long userId) {
        return cropRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Crop getCropByIdAndUserId(Long cropId, Long userId) {
        return cropRepository.findByIdAndUserId(cropId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + cropId));
    }

    @Override
    @Transactional
    public Crop addCrop(Long userId, CropRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Crop crop = Crop.builder()
                .user(user)
                .cropName(request.getCropName())
                .sowingDate(request.getSowingDate())
                .harvestDate(request.getHarvestDate())
                .status(CropStatus.PLANTED)
                .build();

        return cropRepository.save(crop);
    }

    @Override
    @Transactional
    public Crop updateCrop(Long cropId, Long userId, CropRequest request) {
        Crop crop = getCropByIdAndUserId(cropId, userId);
        crop.setCropName(request.getCropName());
        crop.setSowingDate(request.getSowingDate());
        if (request.getHarvestDate() != null) {
            crop.setHarvestDate(request.getHarvestDate());
        }
        return cropRepository.save(crop);
    }

    @Override
    @Transactional
    public void deleteCrop(Long cropId, Long userId) {
        Crop crop = getCropByIdAndUserId(cropId, userId);
        cropRepository.delete(crop);
    }
}
