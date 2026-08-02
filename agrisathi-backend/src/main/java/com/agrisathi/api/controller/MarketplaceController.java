package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.ContactSellerRequest;
import com.agrisathi.api.dto.request.MarketplaceRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.SellerContactResponse;
import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.MarketplaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/marketplace")
@RequiredArgsConstructor
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    /**
     * Browse Available Produce Listings (Supports optional filtering by crop, location, price range)
     */
    @GetMapping("/listings")
    public ResponseEntity<ApiResponse<List<MarketplaceListing>>> browseListings(
            @RequestParam(required = false) String cropName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        List<MarketplaceListing> listings = marketplaceService.browseListings(cropName, location, minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success("Listings retrieved successfully", listings));
    }

    /**
     * Search Produce Listings by Keyword
     */
    @GetMapping("/listings/search")
    public ResponseEntity<ApiResponse<List<MarketplaceListing>>> searchListings(@RequestParam(required = false) String query) {
        List<MarketplaceListing> listings = marketplaceService.searchListings(query);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", listings));
    }

    /**
     * Get Logged-in Farmer's Listings
     */
    @GetMapping("/my-listings")
    public ResponseEntity<ApiResponse<List<MarketplaceListing>>> getMyListings(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<MarketplaceListing> listings = marketplaceService.getMyListings(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("User listings retrieved successfully", listings));
    }

    /**
     * Get Detailed Information for a Single Listing
     */
    @GetMapping("/listings/{listingId}")
    public ResponseEntity<ApiResponse<MarketplaceListing>> getListingById(@PathVariable Long listingId) {
        MarketplaceListing listing = marketplaceService.getListingById(listingId);
        return ResponseEntity.ok(ApiResponse.success("Listing details retrieved successfully", listing));
    }

    /**
     * Create New Produce Listing
     */
    @PostMapping("/listings")
    public ResponseEntity<ApiResponse<MarketplaceListing>> createListing(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody MarketplaceRequest request) {
        MarketplaceListing listing = marketplaceService.createListing(currentUser.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Listing created successfully", listing),
                HttpStatus.CREATED
        );
    }

    /**
     * Update Existing Produce Listing
     */
    @PutMapping("/listings/{listingId}")
    public ResponseEntity<ApiResponse<MarketplaceListing>> updateListing(
            @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody MarketplaceRequest request) {
        MarketplaceListing updated = marketplaceService.updateListing(listingId, currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Listing updated successfully", updated));
    }

    /**
     * Delete Produce Listing
     */
    @DeleteMapping("/listings/{listingId}")
    public ResponseEntity<ApiResponse<Void>> deleteListing(
            @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        marketplaceService.deleteListing(listingId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Listing deleted successfully"));
    }

    /**
     * Contact Produce Seller / Submit Buyer Inquiry
     */
    @PostMapping("/listings/{listingId}/contact")
    public ResponseEntity<ApiResponse<SellerContactResponse>> contactSeller(
            @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ContactSellerRequest request) {
        Long buyerUserId = (currentUser != null) ? currentUser.getId() : null;
        SellerContactResponse contact = marketplaceService.contactSeller(listingId, request, buyerUserId);
        return ResponseEntity.ok(ApiResponse.success("Seller contact details and inquiry generated successfully", contact));
    }
}
