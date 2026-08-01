package com.agrisathi.api.mapper;

import com.agrisathi.api.dto.response.DiseaseScanResponse;
import com.agrisathi.api.model.entity.DiseaseScan;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoMapper {

    public DiseaseScanResponse toDiseaseScanResponse(DiseaseScan scan) {
        if (scan == null) {
            return null;
        }
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
