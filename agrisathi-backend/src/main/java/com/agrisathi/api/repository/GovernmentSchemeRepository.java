package com.agrisathi.api.repository;

import com.agrisathi.api.model.entity.GovernmentScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GovernmentSchemeRepository extends JpaRepository<GovernmentScheme, Long> {

    List<GovernmentScheme> findByIsActiveTrue();

    List<GovernmentScheme> findByStateIgnoreCaseAndIsActiveTrue(String state);

    List<GovernmentScheme> findByTargetCropContainingIgnoreCaseAndIsActiveTrue(String targetCrop);

    @Query("SELECT g FROM GovernmentScheme g WHERE g.isActive = true " +
            "AND (:state IS NULL OR LOWER(g.state) = LOWER(:state) OR LOWER(g.state) = 'all india' OR LOWER(g.state) = 'central') " +
            "AND (:crop IS NULL OR LOWER(g.targetCrop) LIKE LOWER(CONCAT('%', :crop, '%')) OR LOWER(g.targetCrop) LIKE '%all%')")
    List<GovernmentScheme> filterSchemes(
            @Param("state") String state,
            @Param("crop") String crop);
}
