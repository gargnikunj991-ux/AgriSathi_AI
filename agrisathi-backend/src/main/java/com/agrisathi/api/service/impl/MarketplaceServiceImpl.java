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
        log.debug("[MARKETPLACE_GET_ALL] Fetching all AVAILABLE produce listings");
        return listingRepository.findByStatus(ListingStatus.AVAILABLE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketplaceListing> browseListings(String cropName, String location, BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("[MARKETPLACE_BROWSE] Browsing listings with filters - cropName: '{}', location: '{}', minPrice: {}, maxPrice: {}",
                cropName, location, minPrice, maxPrice);
        List<MarketplaceListing> results;
        if (StringUtils.hasText(cropName) || StringUtils.hasText(location) || minPrice != null || maxPrice != null) {
            results = listingRepository.browseListings(
                    StringUtils.hasText(cropName) ? cropName.trim() : null,
                    StringUtils.hasText(location) ? location.trim() : null,
                    minPrice,
                    maxPrice
            );
        } else {
            results = getAllListings();
        }
        log.info("[MARKETPLACE_BROWSE_SUCCESS] Found {} matching listings", results.size());
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketplaceListing> searchListings(String query) {
        log.info("[MARKETPLACE_SEARCH] Searching listings with query: '{}'", query);
        List<MarketplaceListing> results;
        if (!StringUtils.hasText(query)) {
            results = getAllListings();
        } else {
            results = listingRepository.searchListings(query.trim());
        }
        log.info("[MARKETPLACE_SEARCH_SUCCESS] Search for '{}' returned {} listings", query, results.size());
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketplaceListing> getMyListings(Long userId) {
        log.debug("[MARKETPLACE_MY_LISTINGS] Fetching listings for UserID: {}", userId);
        List<MarketplaceListing> myListings = listingRepository.findByUserId(userId);
        log.info("[MARKETPLACE_MY_LISTINGS_SUCCESS] Retrived {} listings for UserID: {}", myListings.size(), userId);
        return myListings;
    }

    @Override
    @Transactional(readOnly = true)
    public MarketplaceListing getListingById(Long listingId) {
        log.debug("[MARKETPLACE_VIEW] Fetching listing details for ListingID: {}", listingId);
        return listingRepository.findById(listingId)
                .orElseThrow(() -> {
                    log.warn("[MARKETPLACE_NOT_FOUND] Listing not found for ListingID: {}", listingId);
                    return new ResourceNotFoundException("Marketplace listing not found with id: " + listingId);
                });
    }

    @Override
    @Transactional
    public MarketplaceListing createListing(Long userId, MarketplaceRequest request) {
        log.info("[MARKETPLACE_CREATE_ATTEMPT] Listing creation initiated by UserID: {}, crop: '{}', qty: {} {}, price: ₹{}",
                userId, request.getCropName(), request.getQuantity(), request.getUnit(), request.getPrice());

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

        MarketplaceListing saved = listingRepository.save(listing);
        log.info("[MARKETPLACE_CREATE_SUCCESS] Created Marketplace ListingID: {} for UserID: {}, crop: '{}', price: ₹{}",
                saved.getId(), userId, saved.getCropName(), saved.getPrice());

        return saved;
    }

    @Override
    @Transactional
    public MarketplaceListing updateListing(Long listingId, Long userId, MarketplaceRequest request) {
        log.info("[MARKETPLACE_UPDATE_ATTEMPT] Updating ListingID: {} by UserID: {}", listingId, userId);
        MarketplaceListing listing = getListingById(listingId);
        if (!listing.getUser().getId().equals(userId)) {
            log.warn("[MARKETPLACE_UNAUTHORIZED] UserID: {} not authorized to update ListingID: {}", userId, listingId);
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

        MarketplaceListing updated = listingRepository.save(listing);
        log.info("[MARKETPLACE_UPDATE_SUCCESS] Updated ListingID: {} by UserID: {}, status: {}", updated.getId(), userId, updated.getStatus());
        return updated;
    }

    @Override
    @Transactional
    public void deleteListing(Long listingId, Long userId) {
        log.info("[MARKETPLACE_DELETE_ATTEMPT] Deleting ListingID: {} by UserID: {}", listingId, userId);
        MarketplaceListing listing = getListingById(listingId);
        if (!listing.getUser().getId().equals(userId)) {
            log.warn("[MARKETPLACE_UNAUTHORIZED] UserID: {} not authorized to delete ListingID: {}", userId, listingId);
            throw new UnauthorizedException("You are not authorized to delete this listing");
        }

        listingRepository.delete(listing);
        log.info("[MARKETPLACE_DELETE_SUCCESS] ListingID: {} deleted successfully by UserID: {}", listingId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public SellerContactResponse contactSeller(Long listingId, ContactSellerRequest request, Long buyerUserId) {
        log.info("[MARKETPLACE_INQUIRY_ATTEMPT] Buyer inquiry submitted for ListingID: {} by buyerUserId: {}", listingId, buyerUserId);
        MarketplaceListing listing = getListingById(listingId);
        User seller = listing.getUser();

        String buyerName = StringUtils.hasText(request.getBuyerName()) ? request.getBuyerName() : "Interested Buyer";
        String inquiryMessage = String.format("Hi %s, %s is interested in your listing '%s' (%.2f %s at ₹%.2f/%s) in %s. Inquiry: %s",
                seller.getName(), buyerName, listing.getCropName(), listing.getQuantity(), listing.getUnit(),
                listing.getPrice(), listing.getUnit(), listing.getLocation(), request.getMessage());

        log.info("[MARKETPLACE_INQUIRY_SUCCESS] Inquiry submitted for listingId: {}: Buyer contact ({}, {}), Seller contact ({}, {})",
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

    @Override
    @Transactional(readOnly = true)
    public SellerContactResponse buyListing(Long listingId, ContactSellerRequest request, Long buyerUserId) {
        log.info("[MARKETPLACE_BUY_ATTEMPT] Purchase request placed for ListingID: {} by buyerUserId: {}", listingId, buyerUserId);
        MarketplaceListing listing = getListingById(listingId);
        User seller = listing.getUser();

        String buyerName = StringUtils.hasText(request.getBuyerName()) ? request.getBuyerName() : "Purchasing Buyer";
        String purchaseMessage = String.format("Purchase Order Confirmed: %s has placed an order to buy produce from listing '%s' (%.2f %s at ₹%.2f/%s) in %s. Buyer note: %s",
                buyerName, listing.getCropName(), listing.getQuantity(), listing.getUnit(),
                listing.getPrice(), listing.getUnit(), listing.getLocation(), request.getMessage());

        log.info("[MARKETPLACE_BUY_SUCCESS] Purchase order placed for listingId: {}: Buyer contact ({}, {}), Seller contact ({}, {})",
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
                .contactStatus("PURCHASE_REQUESTED")
                .formattedInquiry(purchaseMessage)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SellerContactResponse borrowListing(Long listingId, ContactSellerRequest request, Long buyerUserId) {
        log.info("[MARKETPLACE_BORROW_ATTEMPT] Borrow request submitted for ListingID: {} by buyerUserId: {}", listingId, buyerUserId);
        MarketplaceListing listing = getListingById(listingId);
        User seller = listing.getUser();

        String buyerName = StringUtils.hasText(request.getBuyerName()) ? request.getBuyerName() : "Borrowing Buyer";
        String borrowMessage = String.format("Borrow/Rental Request: %s has requested to borrow item from listing '%s' (%.2f %s at ₹%.2f/%s) in %s. Borrow terms: %s",
                buyerName, listing.getCropName(), listing.getQuantity(), listing.getUnit(),
                listing.getPrice(), listing.getUnit(), listing.getLocation(), request.getMessage());

        log.info("[MARKETPLACE_BORROW_SUCCESS] Borrow request submitted for listingId: {}: Buyer contact ({}, {}), Seller contact ({}, {})",
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
                .contactStatus("BORROW_REQUESTED")
                .formattedInquiry(borrowMessage)
                .build();
    }
}
