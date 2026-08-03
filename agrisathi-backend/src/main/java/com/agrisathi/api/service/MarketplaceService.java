package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.ContactSellerRequest;
import com.agrisathi.api.dto.request.MarketplaceRequest;
import com.agrisathi.api.dto.response.SellerContactResponse;
import com.agrisathi.api.model.entity.MarketplaceListing;

import java.math.BigDecimal;
import java.util.List;

public interface MarketplaceService {
    List<MarketplaceListing> getAllListings();
    List<MarketplaceListing> browseListings(String cropName, String location, BigDecimal minPrice, BigDecimal maxPrice);
    List<MarketplaceListing> searchListings(String query);
    List<MarketplaceListing> getMyListings(Long userId);
    MarketplaceListing getListingById(Long listingId);
    MarketplaceListing createListing(Long userId, MarketplaceRequest request);
    MarketplaceListing updateListing(Long listingId, Long userId, MarketplaceRequest request);
    void deleteListing(Long listingId, Long userId);
    SellerContactResponse contactSeller(Long listingId, ContactSellerRequest request, Long buyerUserId);
    SellerContactResponse buyListing(Long listingId, ContactSellerRequest request, Long buyerUserId);
    SellerContactResponse borrowListing(Long listingId, ContactSellerRequest request, Long buyerUserId);
}
