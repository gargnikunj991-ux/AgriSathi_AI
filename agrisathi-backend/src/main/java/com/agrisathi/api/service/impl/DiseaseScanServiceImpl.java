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

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/webp", "image/gif");
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".webp", ".gif");
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
        String originalFilename = image.getOriginalFilename();

        boolean isValidContentType = contentType != null && ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase());
        boolean isValidExtension = false;
        if (originalFilename != null && originalFilename.contains(".")) {
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            isValidExtension = ALLOWED_EXTENSIONS.contains(ext);
        }

        if (!isValidContentType && !isValidExtension) {
            throw new BadRequestException("Invalid image format. Allowed formats: JPG, JPEG, PNG, WEBP, GIF");
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

        // Mock AI Diagnosis engine logic based on filename or defaults
        String disease = "Leaf Rust (Puccinia triticina)";
        double confidence = 98.2;
        String treatment = "Apply Copper Oxychloride 50% WP (2.5g/L) or Tebuconazole fungicide spray every 10-14 days. Ensure adequate plant spacing to maintain canopy ventilation.";

        if (originalFilename != null) {
            String lowerName = originalFilename.toLowerCase();
            if (lowerName.contains("tomato") || lowerName.contains("blight")) {
                disease = "Late Blight (Phytophthora infestans)";
                confidence = 96.5;
                treatment = "Spray Mancozeb 75% WP (2g/L) or Metalaxyl bio-fungicide. Remove and destroy infected leaves immediately.";
            } else if (lowerName.contains("rice") || lowerName.contains("paddy")) {
                disease = "Bacterial Leaf Blight (Xanthomonas oryzae)";
                confidence = 94.8;
                treatment = "Apply Streptocycline (1g in 10L water) combined with Copper Oxychloride (25g in 10L water). Reduce nitrogen application.";
            } else if (lowerName.contains("healthy")) {
                disease = "Healthy Crop (No Disease Detected)";
                confidence = 99.1;
                treatment = "No treatment required. Maintain current irrigation and organic fertilizer schedule.";
            }
        }

        DiseaseScan scan = DiseaseScan.builder()
                .user(user)
                .imageUrl(imageUrl)
                .diseaseName(disease)
                .confidence(confidence)
                .treatment(treatment)
                .build();

        DiseaseScan savedScan = diseaseScanRepository.save(scan);

        return mapToResponse(savedScan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiseaseScanResponse> getScanHistory(Long userId) {
        return diseaseScanRepository.findByUserIdOrderByScannedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DiseaseScanResponse getScanById(Long userId, Long scanId) {
        DiseaseScan scan = diseaseScanRepository.findById(scanId)
                .orElseThrow(() -> new ResourceNotFoundException("Disease scan record not found: " + scanId));

        if (!scan.getUser().getId().equals(userId)) {
            throw new BadRequestException("You are not authorized to view this scan record");
        }

        return mapToResponse(scan);
    }

    private DiseaseScanResponse mapToResponse(DiseaseScan scan) {
        return DiseaseScanResponse.builder()
                .id(scan.getId())
                .imageUrl(scan.getImageUrl())
                .disease(scan.getDiseaseName())
                .confidence(scan.getConfidence())
                .treatment(scan.getTreatment())
                .scannedAt(scan.getScannedAt())
                .build();
    }
}
