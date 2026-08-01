package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.FarmerProfileRequest;
import com.agrisathi.api.exception.ResourceNotFoundException;
import com.agrisathi.api.model.entity.FarmerProfile;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.repository.FarmerProfileRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.FarmerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FarmerProfileServiceImpl implements FarmerProfileService {

    private final FarmerProfileRepository farmerProfileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public FarmerProfile getProfileByUserId(Long userId) {
        return farmerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found for user: " + userId));
    }

    @Override
    @Transactional
    public FarmerProfile createOrUpdateProfile(Long userId, FarmerProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        FarmerProfile profile = farmerProfileRepository.findByUserId(userId)
                .orElseGet(() -> FarmerProfile.builder().user(user).build());

        profile.setState(request.getState());
        profile.setDistrict(request.getDistrict());
        profile.setVillage(request.getVillage());
        profile.setFarmSize(request.getFarmSize());
        profile.setSoilType(request.getSoilType());
        profile.setMainCrop(request.getMainCrop());

        return farmerProfileRepository.save(profile);
    }

    @Override
    @Transactional
    public void deleteProfileByUserId(Long userId) {
        FarmerProfile profile = getProfileByUserId(userId);
        farmerProfileRepository.delete(profile);
    }
}
