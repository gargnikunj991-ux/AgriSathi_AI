package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.MarketplaceRequest;
import com.agrisathi.api.model.entity.MarketplaceListing;

import java.util.List;

public interface MarketplaceService {
    List<MarketplaceListing> getAllListings();
    List<MarketplaceListing> getMyListings(Long userId);
    MarketplaceListing getListingById(Long listingId);
    MarketplaceListing createListing(Long userId, MarketplaceRequest request);
    MarketplaceListing updateListing(Long listingId, Long userId, MarketplaceRequest request);
    void deleteListing(Long listingId, Long userId);
}
