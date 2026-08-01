package com.agrisathi.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseScanResponse {
    private Long id;
    private String imageUrl;
    private String disease;
    private Double confidence;
    private String treatment;
    private LocalDateTime scannedAt;
}
