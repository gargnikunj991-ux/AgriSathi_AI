package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.response.DiseaseScanResponse;
import com.agrisathi.api.exception.BadRequestException;
import com.agrisathi.api.exception.ResourceNotFoundException;
import com.agrisathi.api.model.entity.DiseaseScan;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.repository.DiseaseScanRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.DiseaseScanService;
import com.agrisathi.api.util.CloudinaryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiseaseScanServiceImpl implements DiseaseScanService {

    private final DiseaseScanRepository diseaseScanRepository;
    private final UserRepository userRepository;
    private final CloudinaryUtil cloudinaryUtil;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    @Transactional
    public DiseaseScanResponse scanDisease(Long userId, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        if (image.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum allowed 5MB limit");
        }

        String contentType = image.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("Invalid image format. Allowed formats: JPG, PNG, WEBP");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        String imageUrl;
        try {
            imageUrl = cloudinaryUtil.uploadFile(image);
        } catch (Exception e) {
            log.warn("Cloudinary upload failed, falling back to static URL for demo: {}", e.getMessage());
            imageUrl = "https://cloudinary.com/demo-scan-" + System.currentTimeMillis() + ".jpg";
        }

        // Mock AI Diagnosis Result
        String disease = "Leaf Rust";
        double confidence = 98.2;
        String treatment = "Copper Fungicide spray every 10-14 days. Ensure adequate spacing for canopy airflow.";

        DiseaseScan scan = DiseaseScan.builder()
                .user(user)
                .imageUrl(imageUrl)
                .diseaseName(disease)
                .confidence(confidence)
                .treatment(treatment)
                .build();

        DiseaseScan savedScan = diseaseScanRepository.save(scan);

        return DiseaseScanResponse.builder()
                .id(savedScan.getId())
                .imageUrl(savedScan.getImageUrl())
                .disease(savedScan.getDiseaseName())
                .confidence(savedScan.getConfidence())
                .treatment(savedScan.getTreatment())
                .scannedAt(savedScan.getScannedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiseaseScanResponse> getScanHistory(Long userId) {
        return diseaseScanRepository.findByUserIdOrderByScannedAtDesc(userId).stream()
                .map(scan -> DiseaseScanResponse.builder()
                        .id(scan.getId())
                        .imageUrl(scan.getImageUrl())
                        .disease(scan.getDiseaseName())
                        .confidence(scan.getConfidence())
                        .treatment(scan.getTreatment())
                        .scannedAt(scan.getScannedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
