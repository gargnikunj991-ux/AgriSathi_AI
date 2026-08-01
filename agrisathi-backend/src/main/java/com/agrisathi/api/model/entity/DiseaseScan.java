package com.agrisathi.api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "disease_scans", indexes = {
    @Index(name = "idx_scans_user", columnList = "user_id, scanned_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseaseScan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id")
    private Crop crop;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "disease_name", nullable = false, length = 150)
    private String diseaseName;

    @Column(nullable = false)
    private Double confidence;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String treatment;

    @CreationTimestamp
    @Column(name = "scanned_at", updatable = false)
    private LocalDateTime scannedAt;
}
