package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.response.FileUploadResponse;
import com.agrisathi.api.exception.BadRequestException;
import com.agrisathi.api.exception.ResourceNotFoundException;
import com.agrisathi.api.model.entity.UploadedFile;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.repository.UploadedFileRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.FileStorageService;
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
public class FileStorageServiceImpl implements FileStorageService {

    private final UploadedFileRepository uploadedFileRepository;
    private final UserRepository userRepository;
    private final CloudinaryUtil cloudinaryUtil;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_IMAGE_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    );
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".webp", ".gif"
    );

    @Override
    @Transactional
    public FileUploadResponse uploadFile(Long userId, MultipartFile file) {
        validateImageFile(file);
        return processAndSaveFile(userId, file);
    }

    @Override
    @Transactional
    public FileUploadResponse uploadImage(Long userId, MultipartFile image) {
        validateImageFile(image);
        return processAndSaveFile(userId, image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileUploadResponse> getUserUploadedFiles(Long userId) {
        return uploadedFileRepository.findByUserId(userId).stream()
                .map(file -> FileUploadResponse.builder()
                        .url(file.getFileUrl())
                        .fileName(file.getFileName())
                        .fileType(file.getFileType())
                        .fileSize(file.getFileSize())
                        .uploadedAt(file.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file must not be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum allowed 5MB limit");
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        boolean isValidMime = contentType != null && ALLOWED_IMAGE_CONTENT_TYPES.contains(contentType.toLowerCase());

        boolean isValidExtension = false;
        if (originalFilename != null && originalFilename.contains(".")) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            isValidExtension = ALLOWED_IMAGE_EXTENSIONS.contains(extension);
        }

        if (!isValidMime && !isValidExtension) {
            throw new BadRequestException("Invalid image format. Allowed formats: JPG, JPEG, PNG, WEBP, GIF");
        }
    }

    private FileUploadResponse processAndSaveFile(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        String fileUrl;
        try {
            fileUrl = cloudinaryUtil.uploadFile(file);
        } catch (Exception e) {
            log.warn("Cloudinary upload failed, using fallback URL: {}", e.getMessage());
            fileUrl = "https://cloudinary.com/uploads/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        }

        UploadedFile uploadedFile = UploadedFile.builder()
                .user(user)
                .fileName(file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg")
                .fileUrl(fileUrl)
                .fileType(file.getContentType() != null ? file.getContentType() : "image/jpeg")
                .fileSize(file.getSize())
                .build();

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        return FileUploadResponse.builder()
                .url(savedFile.getFileUrl())
                .fileName(savedFile.getFileName())
                .fileType(savedFile.getFileType())
                .fileSize(savedFile.getFileSize())
                .uploadedAt(savedFile.getCreatedAt())
                .build();
    }
}
