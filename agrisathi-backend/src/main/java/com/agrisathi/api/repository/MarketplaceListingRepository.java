package com.agrisathi.api.repository;

import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.model.enums.ListingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarketplaceListingRepository extends JpaRepository<MarketplaceListing, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<MarketplaceListing> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    List<MarketplaceListing> findByStatus(ListingStatus status);

    @EntityGraph(attributePaths = {"user"})
    Optional<MarketplaceListing> findByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT m FROM MarketplaceListing m WHERE m.status = 'AVAILABLE' " +
            "AND (LOWER(m.cropName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(m.location) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<MarketplaceListing> searchListings(@Param("query") String query);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT m FROM MarketplaceListing m WHERE m.status = 'AVAILABLE' " +
            "AND (:cropName IS NULL OR LOWER(m.cropName) LIKE LOWER(CONCAT('%', :cropName, '%'))) " +
            "AND (:location IS NULL OR LOWER(m.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:minPrice IS NULL OR m.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR m.price <= :maxPrice)")
    List<MarketplaceListing> browseListings(
            @Param("cropName") String cropName,
            @Param("location") String location,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);
}
