package com.agrisathi.api.service;

import com.agrisathi.api.dto.response.DiseaseScanResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DiseaseScanService {
    DiseaseScanResponse scanDisease(Long userId, MultipartFile image);
    List<DiseaseScanResponse> getScanHistory(Long userId);
    DiseaseScanResponse getScanById(Long userId, Long scanId);
}
