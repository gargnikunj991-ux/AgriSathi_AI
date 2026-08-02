package com.agrisathi.api.service;

import com.agrisathi.api.dto.response.FileUploadResponse;
import com.agrisathi.api.exception.BadRequestException;
import com.agrisathi.api.model.entity.UploadedFile;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.repository.UploadedFileRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.impl.FileStorageServiceImpl;
import com.agrisathi.api.util.CloudinaryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @Mock
    private UploadedFileRepository uploadedFileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudinaryUtil cloudinaryUtil;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("testuser@agrisathi.com")
                .passwordHash("Password@123")
                .phone("9876543210")
                .build();
    }

    @Test
    void testUploadValidImage_Success() throws Exception {
        MockMultipartFile validImage = new MockMultipartFile(
                "file",
                "sample_crop.png",
                "image/png",
                "fake-png-content".getBytes()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(cloudinaryUtil.uploadFile(any())).thenReturn("https://cloudinary.com/sample_crop.png");
        when(uploadedFileRepository.save(any())).thenAnswer(invocation -> {
            UploadedFile entity = invocation.getArgument(0);
            entity.setId(10L);
            return entity;
        });

        FileUploadResponse response = fileStorageService.uploadImage(1L, validImage);

        assertNotNull(response);
        assertEquals("https://cloudinary.com/sample_crop.png", response.getUrl());
        assertEquals("sample_crop.png", response.getFileName());
        assertEquals("image/png", response.getFileType());
    }

    @Test
    void testUploadInvalidFileType_ThrowsBadRequestException() {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "document.pdf",
                "application/pdf",
                "fake-pdf-content".getBytes()
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                fileStorageService.uploadImage(1L, invalidFile));

        assertTrue(exception.getMessage().contains("Invalid image format"));
    }

    @Test
    void testUploadExceededFileSize_ThrowsBadRequestException() {
        byte[] largeContent = new byte[6 * 1024 * 1024]; // 6MB
        MockMultipartFile largeImage = new MockMultipartFile(
                "file",
                "large_crop.jpg",
                "image/jpeg",
                largeContent
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                fileStorageService.uploadImage(1L, largeImage));

        assertTrue(exception.getMessage().contains("exceeds maximum allowed 5MB limit"));
    }

    @Test
    void testUploadEmptyFile_ThrowsBadRequestException() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                fileStorageService.uploadImage(1L, emptyFile));

        assertTrue(exception.getMessage().contains("must not be empty"));
    }
}
