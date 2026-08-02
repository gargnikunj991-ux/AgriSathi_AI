package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.ContactSellerRequest;
import com.agrisathi.api.dto.request.MarketplaceRequest;
import com.agrisathi.api.dto.response.SellerContactResponse;
import com.agrisathi.api.exception.ResourceNotFoundException;
import com.agrisathi.api.exception.UnauthorizedException;
import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.model.enums.ListingStatus;
import com.agrisathi.api.repository.MarketplaceListingRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketplaceServiceImpl implements MarketplaceService {

    private final MarketplaceListingRepository listingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MarketplaceListing> getAllListings() {
        return listingRepository.findByStatus(ListingStatus.AVAILABLE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketplaceListing> browseListings(String cropName, String location, BigDecimal minPrice, BigDecimal maxPrice) {
        if (StringUtils.hasText(cropName) || StringUtils.hasText(location) || minPrice != null || maxPrice != null) {
            return listingRepository.browseListings(
                    StringUtils.hasText(cropName) ? cropName.trim() : null,
                    StringUtils.hasText(location) ? location.trim() : null,
                    minPrice,
                    maxPrice
            );
        }
        return getAllListings();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketplaceListing> searchListings(String query) {
        if (!StringUtils.hasText(query)) {
            return getAllListings();
        }
        return listingRepository.searchListings(query.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketplaceListing> getMyListings(Long userId) {
        return listingRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public MarketplaceListing getListingById(Long listingId) {
        return listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Marketplace listing not found with id: " + listingId));
    }

    @Override
    @Transactional
    public MarketplaceListing createListing(Long userId, MarketplaceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        MarketplaceListing listing = MarketplaceListing.builder()
                .user(user)
                .cropName(request.getCropName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .unit(request.getUnit())
                .location(request.getLocation())
                .status(request.getStatus() != null ? request.getStatus() : ListingStatus.AVAILABLE)
                .build();

        return listingRepository.save(listing);
    }

    @Override
    @Transactional
    public MarketplaceListing updateListing(Long listingId, Long userId, MarketplaceRequest request) {
        MarketplaceListing listing = getListingById(listingId);
        if (!listing.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this listing");
        }

        listing.setCropName(request.getCropName());
        listing.setDescription(request.getDescription());
        listing.setImageUrl(request.getImageUrl());
        listing.setQuantity(request.getQuantity());
        listing.setPrice(request.getPrice());
        listing.setUnit(request.getUnit());
        listing.setLocation(request.getLocation());
        if (request.getStatus() != null) {
            listing.setStatus(request.getStatus());
        }

        return listingRepository.save(listing);
    }

    @Override
    @Transactional
    public void deleteListing(Long listingId, Long userId) {
        MarketplaceListing listing = getListingById(listingId);
        if (!listing.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this listing");
        }

        listingRepository.delete(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public SellerContactResponse contactSeller(Long listingId, ContactSellerRequest request, Long buyerUserId) {
        MarketplaceListing listing = getListingById(listingId);
        User seller = listing.getUser();

        String buyerName = StringUtils.hasText(request.getBuyerName()) ? request.getBuyerName() : "Interested Buyer";
        String inquiryMessage = String.format("Hi %s, %s is interested in your listing '%s' (%.2f %s at ₹%.2f/%s) in %s. Inquiry: %s",
                seller.getName(), buyerName, listing.getCropName(), listing.getQuantity(), listing.getUnit(),
                listing.getPrice(), listing.getUnit(), listing.getLocation(), request.getMessage());

        log.info("Inquiry submitted for listing {}: Buyer contact ({}, {}), Seller contact ({}, {})",
                listingId, request.getBuyerPhone(), request.getBuyerEmail(), seller.getPhone(), seller.getEmail());

        return SellerContactResponse.builder()
                .listingId(listing.getId())
                .cropName(listing.getCropName())
                .quantity(listing.getQuantity())
                .price(listing.getPrice())
                .unit(listing.getUnit())
                .location(listing.getLocation())
                .sellerName(seller.getName())
                .sellerPhone(seller.getPhone())
                .sellerEmail(seller.getEmail())
                .contactStatus("INQUIRY_SENT")
                .formattedInquiry(inquiryMessage)
                .build();
    }
}
