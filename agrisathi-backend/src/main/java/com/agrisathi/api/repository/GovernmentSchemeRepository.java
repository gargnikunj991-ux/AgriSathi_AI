package com.agrisathi.api.repository;

import com.agrisathi.api.model.entity.GovernmentScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GovernmentSchemeRepository extends JpaRepository<GovernmentScheme, Long> {
    List<GovernmentScheme> findByIsActiveTrue();
    List<GovernmentScheme> findByStateIgnoreCaseAndIsActiveTrue(String state);
}
