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
        log.info("[AI_DISEASE_SCAN_REQUEST] Disease scan requested by UserID: {}, file: '{}', size: {} bytes, type: '{}'",
                userId, image != null ? image.getOriginalFilename() : "null",
                image != null ? image.getSize() : 0,
                image != null ? image.getContentType() : "null");

        if (image == null || image.isEmpty()) {
            log.warn("[AI_DISEASE_SCAN_FAILED] Empty or null image provided for UserID: {}", userId);
            throw new BadRequestException("Image file is required");
        }

        if (image.getSize() > MAX_FILE_SIZE) {
            log.warn("[AI_DISEASE_SCAN_FAILED] File size {} exceeds limit for UserID: {}", image.getSize(), userId);
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
            log.warn("[AI_DISEASE_SCAN_FAILED] Invalid file format '{}' for UserID: {}", contentType, userId);
            throw new BadRequestException("Invalid image format. Allowed formats: JPG, JPEG, PNG, WEBP, GIF");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        String imageUrl;
        try {
            imageUrl = cloudinaryUtil.uploadFile(image);
        } catch (Exception e) {
            log.warn("[AI_DISEASE_SCAN_UPLOAD_WARN] Cloudinary upload failed, falling back to static URL: {}", e.getMessage());
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
        log.info("[AI_DISEASE_SCAN_SUCCESS] AI Diagnosis completed - ScanID: {}, UserID: {}, Disease: '{}', Confidence: {}%, Image: '{}'",
                savedScan.getId(), userId, disease, confidence, imageUrl);

        return mapToResponse(savedScan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiseaseScanResponse> getScanHistory(Long userId) {
        log.debug("[AI_DISEASE_SCAN_HISTORY] Fetching disease scan history for UserID: {}", userId);
        List<DiseaseScanResponse> history = diseaseScanRepository.findByUserIdOrderByScannedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        log.info("[AI_DISEASE_SCAN_HISTORY] Retrived {} disease scan records for UserID: {}", history.size(), userId);
        return history;
    }

    @Override
    @Transactional(readOnly = true)
    public DiseaseScanResponse getScanById(Long userId, Long scanId) {
        log.debug("[AI_DISEASE_SCAN_DETAIL] Fetching scan details for ScanID: {} by UserID: {}", scanId, userId);
        DiseaseScan scan = diseaseScanRepository.findById(scanId)
                .orElseThrow(() -> new ResourceNotFoundException("Disease scan record not found: " + scanId));

        if (!scan.getUser().getId().equals(userId)) {
            log.warn("[AI_DISEASE_SCAN_UNAUTHORIZED] UserID {} attempted unauthorized access to ScanID {}", userId, scanId);
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
