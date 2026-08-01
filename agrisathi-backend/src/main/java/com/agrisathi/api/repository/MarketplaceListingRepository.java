package com.agrisathi.api.repository;

import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.model.enums.ListingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketplaceListingRepository extends JpaRepository<MarketplaceListing, Long> {
    List<MarketplaceListing> findByUserId(Long userId);
    List<MarketplaceListing> findByStatus(ListingStatus status);
    Optional<MarketplaceListing> findByIdAndUserId(Long id, Long userId);
}
