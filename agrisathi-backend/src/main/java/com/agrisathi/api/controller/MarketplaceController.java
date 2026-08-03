package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.ContactSellerRequest;
import com.agrisathi.api.dto.request.MarketplaceRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.MarketplaceListingResponse;
import com.agrisathi.api.dto.response.SellerContactResponse;
import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.MarketplaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/marketplace")
@RequiredArgsConstructor
@Tag(name = "Marketplace APIs", description = "Endpoints for direct farmer-to-buyer produce listings, search, filtering, and inquiry contacts")
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    @GetMapping("/listings")
    @Operation(summary = "Browse Produce Listings", description = "Browse produce marketplace listings with optional filters for crop name, district location, and price range.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<List<MarketplaceListingResponse>>> browseListings(
            @Parameter(description = "Filter by crop name (e.g., Rice, Wheat)", example = "Rice") @RequestParam(required = false) String cropName,
            @Parameter(description = "Filter by district or location", example = "Dehradun") @RequestParam(required = false) String location,
            @Parameter(description = "Minimum price threshold", example = "20.00") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price threshold", example = "50.00") @RequestParam(required = false) BigDecimal maxPrice) {
        List<MarketplaceListingResponse> listings = marketplaceService.browseListings(cropName, location, minPrice, maxPrice).stream()
                .map(MarketplaceListingResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Listings retrieved successfully", listings));
    }

    @GetMapping("/listings/search")
    @Operation(summary = "Search Produce Listings", description = "Performs keyword search across crop names, descriptions, and locations.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<MarketplaceListingResponse>>> searchListings(
            @Parameter(description = "Keyword search query", example = "Organic Basmati") @RequestParam(required = false) String query) {
        List<MarketplaceListingResponse> listings = marketplaceService.searchListings(query).stream()
                .map(MarketplaceListingResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", listings));
    }

    @GetMapping("/my-listings")
    @Operation(summary = "Get Authenticated Farmer's Listings", description = "Fetches all produce listings created by the currently logged-in farmer.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User listings retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<ApiResponse<List<MarketplaceListingResponse>>> getMyListings(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<MarketplaceListingResponse> listings = marketplaceService.getMyListings(currentUser.getId()).stream()
                .map(MarketplaceListingResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("User listings retrieved successfully", listings));
    }

    @GetMapping("/listings/{listingId}")
    @Operation(summary = "Get Listing Details", description = "Retrieves full details for a single produce listing by ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listing details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Listing not found")
    })
    public ResponseEntity<ApiResponse<MarketplaceListingResponse>> getListingById(
            @Parameter(description = "Listing ID", example = "1") @PathVariable Long listingId) {
        MarketplaceListing listing = marketplaceService.getListingById(listingId);
        return ResponseEntity.ok(ApiResponse.success("Listing details retrieved successfully", MarketplaceListingResponse.fromEntity(listing)));
    }

    @PostMapping("/listings")
    @Operation(summary = "Create Produce Listing", description = "Creates a new produce marketplace listing with quantity, price, unit, and Cloudinary image.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Listing created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failure")
    })
    public ResponseEntity<ApiResponse<MarketplaceListingResponse>> createListing(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody MarketplaceRequest request) {
        MarketplaceListing listing = marketplaceService.createListing(currentUser.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Listing created successfully", MarketplaceListingResponse.fromEntity(listing)),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/listings/{listingId}")
    @Operation(summary = "Update Produce Listing", description = "Updates price, quantity, or description of an existing produce listing.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listing updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized or not listing owner"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Listing not found")
    })
    public ResponseEntity<ApiResponse<MarketplaceListingResponse>> updateListing(
            @Parameter(description = "Listing ID to update", example = "1") @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody MarketplaceRequest request) {
        MarketplaceListing updated = marketplaceService.updateListing(listingId, currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Listing updated successfully", MarketplaceListingResponse.fromEntity(updated)));
    }

    @DeleteMapping("/listings/{listingId}")
    @Operation(summary = "Delete Produce Listing", description = "Deletes a produce marketplace listing.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listing deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized or not listing owner"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Listing not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteListing(
            @Parameter(description = "Listing ID to delete", example = "1") @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        marketplaceService.deleteListing(listingId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Listing deleted successfully"));
    }

    @PostMapping("/listings/{listingId}/contact")
    @Operation(summary = "Contact Seller / Submit Inquiry", description = "Generates formatted purchase inquiry and retrieves seller contact information.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Seller contact details generated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Listing not found")
    })
    public ResponseEntity<ApiResponse<SellerContactResponse>> contactSeller(
            @Parameter(description = "Listing ID", example = "1") @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ContactSellerRequest request) {
        Long buyerUserId = (currentUser != null) ? currentUser.getId() : null;
        SellerContactResponse contact = marketplaceService.contactSeller(listingId, request, buyerUserId);
        return ResponseEntity.ok(ApiResponse.success("Seller contact details and inquiry generated successfully", contact));
    }

    @PostMapping("/listings/{listingId}/buy")
    @Operation(summary = "Buy Produce Listing", description = "Submits a purchase order for a marketplace listing.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Purchase request submitted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Listing not found")
    })
    public ResponseEntity<ApiResponse<SellerContactResponse>> buyListing(
            @Parameter(description = "Listing ID", example = "1") @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ContactSellerRequest request) {
        Long buyerUserId = (currentUser != null) ? currentUser.getId() : null;
        SellerContactResponse result = marketplaceService.buyListing(listingId, request, buyerUserId);
        return ResponseEntity.ok(ApiResponse.success("Purchase order submitted successfully", result));
    }

    @PostMapping("/listings/{listingId}/borrow")
    @Operation(summary = "Borrow Marketplace Item", description = "Submits a borrowing/rental request for a marketplace listing.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Borrow request submitted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Listing not found")
    })
    public ResponseEntity<ApiResponse<SellerContactResponse>> borrowListing(
            @Parameter(description = "Listing ID", example = "1") @PathVariable Long listingId,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ContactSellerRequest request) {
        Long buyerUserId = (currentUser != null) ? currentUser.getId() : null;
        SellerContactResponse result = marketplaceService.borrowListing(listingId, request, buyerUserId);
        return ResponseEntity.ok(ApiResponse.success("Borrow request submitted successfully", result));
    }
}
