package com.agrisathi.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "AI disease scan diagnosis response payload")
public class DiseaseScanResponse {

    @Schema(description = "Unique disease scan record ID", example = "1")
    private Long id;

    @Schema(description = "Cloudinary image URL", example = "https://res.cloudinary.com/agrisathi/image/upload/v123/leaf_rust.jpg")
    private String imageUrl;

    @Schema(description = "Diagnosed plant disease name", example = "Leaf Rust")
    private String disease;

    @Schema(description = "AI diagnostic confidence score percentage", example = "98.2")
    private Double confidence;

    @Schema(description = "Recommended treatment plan & fungicide advisory", example = "Apply Copper Fungicide (2g/L water) or Mancozeb spray.")
    private String treatment;

    @Schema(description = "Scan completion timestamp", example = "2026-08-03T14:30:00")
    private LocalDateTime scannedAt;
}
