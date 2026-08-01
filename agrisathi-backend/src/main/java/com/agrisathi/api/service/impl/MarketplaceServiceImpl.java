package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.MarketplaceRequest;
import com.agrisathi.api.exception.ResourceNotFoundException;
import com.agrisathi.api.exception.UnauthorizedException;
import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.model.enums.ListingStatus;
import com.agrisathi.api.repository.MarketplaceListingRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .unit(request.getUnit())
                .location(request.getLocation())
                .status(ListingStatus.AVAILABLE)
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
        listing.setQuantity(request.getQuantity());
        listing.setPrice(request.getPrice());
        listing.setUnit(request.getUnit());
        listing.setLocation(request.getLocation());

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
}
