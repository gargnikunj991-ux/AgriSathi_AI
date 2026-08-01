package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.FarmerProfileRequest;
import com.agrisathi.api.model.entity.FarmerProfile;

public interface FarmerProfileService {
    FarmerProfile getProfileByUserId(Long userId);
    FarmerProfile createOrUpdateProfile(Long userId, FarmerProfileRequest request);
    void deleteProfileByUserId(Long userId);
}
