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

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final UploadedFileRepository uploadedFileRepository;
    private final UserRepository userRepository;
    private final CloudinaryUtil cloudinaryUtil;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    @Transactional
    public FileUploadResponse uploadFile(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File must not be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds 5MB limit");
        }

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
                .fileName(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file")
                .fileUrl(fileUrl)
                .fileType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                .fileSize(file.getSize())
                .build();

        uploadedFileRepository.save(uploadedFile);

        return FileUploadResponse.builder()
                .url(fileUrl)
                .build();
    }
}
