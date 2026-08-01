package com.agrisathi.api.repository;

import com.agrisathi.api.model.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> findByUserId(Long userId);
    Optional<Crop> findByIdAndUserId(Long id, Long userId);
}
