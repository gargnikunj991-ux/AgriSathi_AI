package com.agrisathi.api.repository;

import com.agrisathi.api.model.entity.DiseaseScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseScanRepository extends JpaRepository<DiseaseScan, Long> {
    List<DiseaseScan> findByUserIdOrderByScannedAtDesc(Long userId);
}
