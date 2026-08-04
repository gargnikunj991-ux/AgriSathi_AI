package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.ContactSellerRequest;
import com.agrisathi.api.dto.request.MarketplaceRequest;
import com.agrisathi.api.dto.response.SellerContactResponse;
import com.agrisathi.api.model.entity.MarketplaceListing;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.model.enums.ListingStatus;
import com.agrisathi.api.repository.MarketplaceListingRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.impl.MarketplaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MarketplaceServiceTest {

    @Mock
    private MarketplaceListingRepository listingRepository;

    @Mock
    private UserRepository userRepository;

    private MarketplaceServiceImpl marketplaceService;

    @BeforeEach
    void setUp() {
        marketplaceService = new MarketplaceServiceImpl(listingRepository, userRepository);
    }

    @Test
    @DisplayName("createListing - Should create and save marketplace listing")
    void testCreateListing() {
        User user = User.builder().id(1L).name("Farmer Ramesh").build();
        MarketplaceRequest request = MarketplaceRequest.builder()
                .cropName("Rice")
                .quantity(BigDecimal.valueOf(100))
                .price(BigDecimal.valueOf(40))
                .unit("kg")
                .location("Dehradun")
                .build();

        MarketplaceListing saved = MarketplaceListing.builder()
                .id(10L)
                .user(user)
                .cropName("Rice")
                .quantity(BigDecimal.valueOf(100))
                .price(BigDecimal.valueOf(40))
                .unit("kg")
                .location("Dehradun")
                .status(ListingStatus.AVAILABLE)
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(listingRepository.save(any(MarketplaceListing.class))).willReturn(saved);

        MarketplaceListing result = marketplaceService.createListing(1L, request);

        assertThat(result).isNotNull();
        assertThat(result.getCropName()).isEqualTo("Rice");
        verify(listingRepository).save(any(MarketplaceListing.class));
    }

    @Test
    @DisplayName("contactSeller - Should generate seller contact info & inquiry")
    void testContactSeller() {
        User seller = User.builder()
                .id(2L)
                .name("Seller Suresh")
                .phone("9876543210")
                .email("suresh@agrisathi.com")
                .build();

        MarketplaceListing listing = MarketplaceListing.builder()
                .id(5L)
                .user(seller)
                .cropName("Wheat")
                .quantity(BigDecimal.valueOf(200))
                .price(BigDecimal.valueOf(30))
                .unit("kg")
                .location("Haridwar")
                .build();

        ContactSellerRequest request = ContactSellerRequest.builder()
                .buyerName("Buyer Anita")
                .message("Interested in buying 200kg wheat")
                .build();

        given(listingRepository.findById(5L)).willReturn(Optional.of(listing));

        SellerContactResponse response = marketplaceService.contactSeller(5L, request, 1L);

        assertThat(response).isNotNull();
        assertThat(response.getSellerName()).isEqualTo("Seller Suresh");
        assertThat(response.getSellerPhone()).isEqualTo("9876543210");
        assertThat(response.getContactStatus()).isEqualTo("INQUIRY_SENT");
    }

    @Test
    @DisplayName("buyListing - Should generate purchase order response")
    void testBuyListing() {
        User seller = User.builder()
                .id(2L)
                .name("Seller Suresh")
                .phone("9876543210")
                .email("suresh@agrisathi.com")
                .build();

        MarketplaceListing listing = MarketplaceListing.builder()
                .id(5L)
                .user(seller)
                .cropName("Wheat")
                .quantity(BigDecimal.valueOf(200))
                .price(BigDecimal.valueOf(30))
                .unit("kg")
                .location("Haridwar")
                .build();

        ContactSellerRequest request = ContactSellerRequest.builder()
                .buyerName("Buyer Ramesh")
                .message("Purchasing 200kg wheat")
                .build();

        given(listingRepository.findById(5L)).willReturn(Optional.of(listing));

        SellerContactResponse response = marketplaceService.buyListing(5L, request, 1L);

        assertThat(response).isNotNull();
        assertThat(response.getSellerName()).isEqualTo("Seller Suresh");
        assertThat(response.getContactStatus()).isEqualTo("PURCHASE_REQUESTED");
    }

    @Test
    @DisplayName("borrowListing - Should generate borrow request response")
    void testBorrowListing() {
        User seller = User.builder()
                .id(2L)
                .name("Seller Suresh")
                .phone("9876543210")
                .email("suresh@agrisathi.com")
                .build();

        MarketplaceListing listing = MarketplaceListing.builder()
                .id(5L)
                .user(seller)
                .cropName("Tractor Equipment")
                .quantity(BigDecimal.valueOf(1))
                .price(BigDecimal.valueOf(500))
                .unit("day")
                .location("Haridwar")
                .build();

        ContactSellerRequest request = ContactSellerRequest.builder()
                .buyerName("Buyer Ramesh")
                .message("Request to borrow tractor for 2 days")
                .build();

        given(listingRepository.findById(5L)).willReturn(Optional.of(listing));

        SellerContactResponse response = marketplaceService.borrowListing(5L, request, 1L);

        assertThat(response).isNotNull();
        assertThat(response.getSellerName()).isEqualTo("Seller Suresh");
        assertThat(response.getContactStatus()).isEqualTo("BORROW_REQUESTED");
    }
}
