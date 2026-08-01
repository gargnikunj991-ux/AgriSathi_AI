package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.MarketplaceRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.MarketplaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/marketplace")
@RequiredArgsConstructor
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    @GetMapping("/listings")
    public ResponseEntity<ApiResponse<List<MarketplaceListing>>> getAllListings() {
        List<MarketplaceListing> listings = marketplaceService.getAllListings();
        return ResponseEntity.ok(ApiResponse.success("Listings retrieved successfully", listings));
    }

    @GetMapping("/my-listings")
    public ResponseEntity<ApiResponse<List<MarketplaceListing>>> getMyListings(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<MarketplaceListing> listings = marketplaceService.getMyListings(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("User listings retrieved successfully", listings));
    }

    @GetMapping("/listings/{listingId}")
    public ResponseEntity<ApiResponse<MarketplaceListing>> getListingById(@PathVariable Long listingId) {
        MarketplaceListing listing = marketplaceService.getListingById(listingId);
        return ResponseEntity.ok(ApiResponse.success("Listing details retrieved successfully", listing));
    }

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

    @PutMapping("/listings/{listingId}")
    public ResponseEntity<ApiResponse<MarketplaceListing>> updateListing(
            @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody MarketplaceRequest request) {
        MarketplaceListing updated = marketplaceService.updateListing(listingId, currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Listing updated successfully", updated));
    }

    @DeleteMapping("/listings/{listingId}")
    public ResponseEntity<ApiResponse<Void>> deleteListing(
            @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        marketplaceService.deleteListing(listingId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Listing deleted successfully"));
    }
}
