package com.agrisathi.api;

import com.agrisathi.api.dto.request.*;
import com.agrisathi.api.model.enums.CropStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class Phase17ComprehensiveTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String farmerToken;
    private String farmerEmail;
    private String buyerToken;
    private String buyerEmail;

    @BeforeEach
    void setUp() throws Exception {
        long timestamp = System.currentTimeMillis();

        // 1. Setup Farmer Account
        farmerEmail = "farmer_p17_" + timestamp + "@agrisathi.com";
        RegisterRequest farmerReg = RegisterRequest.builder()
                .name("Farmer Ramesh")
                .email(farmerEmail)
                .password("FarmerPass@123")
                .phone("9876543210")
                .role("FARMER")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(farmerReg)))
                .andExpect(status().isCreated());

        farmerToken = obtainToken(farmerEmail, "FarmerPass@123");

        // 2. Setup Buyer Account
        buyerEmail = "buyer_p17_" + timestamp + "@agrisathi.com";
        RegisterRequest buyerReg = RegisterRequest.builder()
                .name("Buyer Suresh")
                .email(buyerEmail)
                .password("BuyerPass@123")
                .phone("9876543211")
                .role("BUYER")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buyerReg)))
                .andExpect(status().isCreated());

        buyerToken = obtainToken(buyerEmail, "BuyerPass@123");

        // 3. Setup Admin Account
        String adminEmail = "admin_p17_" + timestamp + "@agrisathi.com";
        RegisterRequest adminReg = RegisterRequest.builder()
                .name("Admin System")
                .email(adminEmail)
                .password("AdminPass@123")
                .phone("9876543212")
                .role("ADMIN")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminReg)))
                .andExpect(status().isCreated());
    }

    private String obtainToken(String email, String password) throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        String responseStr = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(responseStr);
        return root.get("data").get("accessToken").asText();
    }

    // =========================================================================
    // 1. HAPPY PATH TESTS
    // =========================================================================
    @Nested
    @DisplayName("1. Happy Path Test Suite")
    class HappyPathTests {

        @Test
        @DisplayName("Happy Path: User Authentication & Profile Fetch")
        void testAuthAndProfileFetch() throws Exception {
            mockMvc.perform(get("/api/v1/auth/me")
                    .header("Authorization", "Bearer " + farmerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.email").value(farmerEmail))
                    .andExpect(jsonPath("$.data.role").value("ROLE_FARMER"));
        }

        @Test
        @DisplayName("Happy Path: Farmer Profile & Crop Creation")
        void testFarmerProfileAndCropCreation() throws Exception {
            // Create Farmer Profile
            FarmerProfileRequest profileReq = FarmerProfileRequest.builder()
                    .state("Uttarakhand")
                    .district("Dehradun")
                    .village("Rampur")
                    .farmSize(new BigDecimal("5.5"))
                    .soilType("Loamy")
                    .mainCrop("Wheat")
                    .build();

            mockMvc.perform(post("/api/v1/farmer/profile")
                    .header("Authorization", "Bearer " + farmerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(profileReq)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.district").value("Dehradun"));

            // Add Crop
            CropRequest cropReq = CropRequest.builder()
                    .cropName("Wheat (HD-2967)")
                    .sowingDate(LocalDate.now().minusDays(30))
                    .harvestDate(LocalDate.now().plusDays(90))
                    .status(CropStatus.PLANTED)
                    .build();

            mockMvc.perform(post("/api/v1/crops")
                    .header("Authorization", "Bearer " + farmerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cropReq)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.cropName").value("Wheat (HD-2967)"));

            // Get Crops List
            mockMvc.perform(get("/api/v1/crops")
                    .header("Authorization", "Bearer " + farmerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))));
        }

        @Test
        @DisplayName("Happy Path: AI Crop Disease Scan")
        void testAiDiseaseScan() throws Exception {
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "wheat_leaf_rust.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "dummy leaf image content".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/disease/scan")
                    .file(file)
                    .header("Authorization", "Bearer " + farmerToken))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.disease").exists())
                    .andExpect(jsonPath("$.data.confidence").value(greaterThan(80.0)))
                    .andExpect(jsonPath("$.data.treatment").exists());
        }

        @Test
        @DisplayName("Happy Path: AI Chat Advisory Assistant")
        void testAiChatAssistant() throws Exception {
            ChatRequest chatReq = new ChatRequest();
            chatReq.setMessage("How much fertilizer should I apply for Basmati Rice?");

            mockMvc.perform(post("/api/v1/chat")
                    .header("Authorization", "Bearer " + farmerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(chatReq)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.response").exists());

            // Fetch chat history
            mockMvc.perform(get("/api/v1/chat/history")
                    .header("Authorization", "Bearer " + farmerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))));
        }

        @Test
        @DisplayName("Happy Path: Weather Advisory & Farming Summary")
        void testWeatherAdvisories() throws Exception {
            mockMvc.perform(get("/api/v1/weather/current")
                    .header("Authorization", "Bearer " + farmerToken)
                    .param("lat", "30.3165")
                    .param("lon", "78.0322"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.temperature").exists());

            mockMvc.perform(get("/api/v1/weather/farming-summary")
                    .header("Authorization", "Bearer " + farmerToken)
                    .param("lat", "30.3165")
                    .param("lon", "78.0322"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.irrigationAdvice").exists())
                    .andExpect(jsonPath("$.data.sprayingCondition").exists());
        }

        @Test
        @DisplayName("Happy Path: Marketplace Produce Listing Creation & Inquiries")
        void testMarketplaceWorkflow() throws Exception {
            MarketplaceRequest req = MarketplaceRequest.builder()
                    .cropName("Organic Basmati Rice")
                    .description("Freshly harvested Basmati Rice grade A")
                    .quantity(new BigDecimal("100.0"))
                    .price(new BigDecimal("45.50"))
                    .unit("kg")
                    .location("Dehradun")
                    .build();

            String responseStr = mockMvc.perform(post("/api/v1/marketplace/listings")
                    .header("Authorization", "Bearer " + farmerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.cropName").value("Organic Basmati Rice"))
                    .andReturn().getResponse().getContentAsString();

            Long listingId = objectMapper.readTree(responseStr).get("data").get("id").asLong();

            // Buyer contacts seller
            ContactSellerRequest contactReq = ContactSellerRequest.builder()
                    .buyerName("Buyer Suresh")
                    .buyerPhone("9876543211")
                    .buyerEmail(buyerEmail)
                    .message("Interested in buying 50 kg")
                    .build();

            mockMvc.perform(post("/api/v1/marketplace/listings/" + listingId + "/contact")
                    .header("Authorization", "Bearer " + buyerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(contactReq)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.contactStatus").value("INQUIRY_SENT"));
        }
    }

    // =========================================================================
    // 2. INVALID INPUT TESTS
    // =========================================================================
    @Nested
    @DisplayName("2. Invalid Input Test Suite")
    class InvalidInputTests {

        @Test
        @DisplayName("Invalid Input: Registration with Invalid Email & Missing Fields")
        void testInvalidRegistrationPayload() throws Exception {
            RegisterRequest invalidReg = RegisterRequest.builder()
                    .name("")
                    .email("not-an-email")
                    .password("short")
                    .phone("123")
                    .role("INVALID_ROLE")
                    .build();

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidReg)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("Invalid Input: Login with Blank Credentials")
        void testInvalidLoginPayload() throws Exception {
            LoginRequest invalidLogin = LoginRequest.builder()
                    .email("")
                    .password("")
                    .build();

            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidLogin)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Invalid Input: Marketplace Listing with Negative Price & Quantity")
        void testInvalidMarketplaceListing() throws Exception {
            MarketplaceRequest req = MarketplaceRequest.builder()
                    .cropName("")
                    .quantity(new BigDecimal("-10.0"))
                    .price(new BigDecimal("-5.0"))
                    .unit("kg")
                    .location("")
                    .build();

            mockMvc.perform(post("/api/v1/marketplace/listings")
                    .header("Authorization", "Bearer " + farmerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Invalid Input: Disease Scan with Unsupported File Type")
        void testInvalidDiseaseScanFile() throws Exception {
            MockMultipartFile textFile = new MockMultipartFile(
                    "image",
                    "malicious.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    "invalid content".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/disease/scan")
                    .file(textFile)
                    .header("Authorization", "Bearer " + farmerToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("Invalid Input: Crop Advice Request Missing Mandatory Parameters")
        void testInvalidCropAdviceRequest() throws Exception {
            CropAdviceRequest invalidAdviceReq = CropAdviceRequest.builder()
                    .state("")
                    .district("")
                    .soilType("")
                    .build();

            mockMvc.perform(post("/api/v1/recommendations/crop-advice")
                    .header("Authorization", "Bearer " + farmerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidAdviceReq)))
                    .andExpect(status().isBadRequest());
        }
    }

    // =========================================================================
    // 3. UNAUTHORIZED ACCESS TESTS
    // =========================================================================
    @Nested
    @DisplayName("3. Unauthorized Access Test Suite")
    class UnauthorizedAccessTests {

        @Test
        @DisplayName("Unauthorized Access: Unauthenticated Endpoint Requests Return 401")
        void testUnauthenticatedAccess() throws Exception {
            mockMvc.perform(get("/api/v1/auth/me"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.success").value(false));

            mockMvc.perform(get("/api/v1/crops"))
                    .andExpect(status().isUnauthorized());

            mockMvc.perform(get("/api/v1/chat/history"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Unauthorized Access: Malformed or Expired JWT Token Return 401")
        void testMalformedJwtToken() throws Exception {
            mockMvc.perform(get("/api/v1/auth/me")
                    .header("Authorization", "Bearer invalid.jwt.token.here"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Unauthorized Access: Buyer Attempting Farmer-Only Feature Returns 403")
        void testBuyerAccessingFarmerFeatures() throws Exception {
            CropRequest cropReq = CropRequest.builder()
                    .cropName("Rice")
                    .sowingDate(LocalDate.now())
                    .build();

            mockMvc.perform(post("/api/v1/crops")
                    .header("Authorization", "Bearer " + buyerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cropReq)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Unauthorized Access: Farmer Attempting Admin-Only Endpoint Returns 403")
        void testFarmerAccessingAdminFeatures() throws Exception {
            mockMvc.perform(get("/api/v1/admin/users")
                    .header("Authorization", "Bearer " + farmerToken))
                    .andExpect(status().isForbidden());
        }
    }

    // =========================================================================
    // 4. MISSING RESOURCES TESTS
    // =========================================================================
    @Nested
    @DisplayName("4. Missing Resources Test Suite")
    class MissingResourcesTests {

        @Test
        @DisplayName("Missing Resource: Non-Existent Crop ID Returns 404")
        void testGetNonExistentCrop() throws Exception {
            mockMvc.perform(get("/api/v1/crops/999999")
                    .header("Authorization", "Bearer " + farmerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("Missing Resource: Non-Existent Marketplace Listing ID Returns 404")
        void testGetNonExistentListing() throws Exception {
            mockMvc.perform(get("/api/v1/marketplace/listings/999999")
                    .header("Authorization", "Bearer " + buyerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("Missing Resource: Non-Existent Disease Scan Record Returns 404")
        void testGetNonExistentDiseaseScan() throws Exception {
            mockMvc.perform(get("/api/v1/disease/scan/999999")
                    .header("Authorization", "Bearer " + farmerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("Missing Resource: Non-Existent Government Scheme Returns 404")
        void testGetNonExistentScheme() throws Exception {
            mockMvc.perform(get("/api/v1/government-schemes/999999")
                    .header("Authorization", "Bearer " + farmerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    // =========================================================================
    // 5. API INTEGRATION TESTS (END-TO-END WORKFLOW)
    // =========================================================================
    @Nested
    @DisplayName("5. API Integration Test Suite (End-to-End)")
    class ApiIntegrationTests {

        @Test
        @DisplayName("API Integration: Complete End-to-End Farmer-to-Buyer Lifecycle")
        void testCompleteEndToEndLifecycle() throws Exception {
            long ts = System.currentTimeMillis();

            // Step 1: Farmer Registration
            String fEmail = "e2e_farmer_" + ts + "@agrisathi.com";
            RegisterRequest fReg = RegisterRequest.builder()
                    .name("E2E Farmer Vijay")
                    .email(fEmail)
                    .password("Pass@12345")
                    .phone("9123456780")
                    .role("FARMER")
                    .build();

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(fReg)))
                    .andExpect(status().isCreated());

            String fToken = obtainToken(fEmail, "Pass@12345");

            // Step 2: Farmer Setup Profile & Add Crop
            FarmerProfileRequest prof = FarmerProfileRequest.builder()
                    .state("Punjab")
                    .district("Ludhiana")
                    .village("Khanna")
                    .farmSize(new BigDecimal("12.0"))
                    .soilType("Alluvial")
                    .mainCrop("Wheat")
                    .build();

            mockMvc.perform(post("/api/v1/farmer/profile")
                    .header("Authorization", "Bearer " + fToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(prof)))
                    .andExpect(status().isCreated());

            // Step 3: Farmer Performs Crop Disease Diagnostic Scan
            MockMultipartFile cropImg = new MockMultipartFile(
                    "image",
                    "paddy_leaf.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "paddy image".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/disease/scan")
                    .file(cropImg)
                    .header("Authorization", "Bearer " + fToken))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.disease").exists());

            // Step 4: Farmer Posts Produce on Marketplace
            MarketplaceRequest listingReq = MarketplaceRequest.builder()
                    .cropName("Premium Wheat HD-2967")
                    .description("High grade wheat, moisture content < 12%")
                    .quantity(new BigDecimal("500.0"))
                    .price(new BigDecimal("2250.00"))
                    .unit("Quintal")
                    .location("Ludhiana, Punjab")
                    .build();

            String createRes = mockMvc.perform(post("/api/v1/marketplace/listings")
                    .header("Authorization", "Bearer " + fToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(listingReq)))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            Long listingId = objectMapper.readTree(createRes).get("data").get("id").asLong();

            // Step 5: Buyer Registers & Searches Marketplace
            String bEmail = "e2e_buyer_" + ts + "@agrisathi.com";
            RegisterRequest bReg = RegisterRequest.builder()
                    .name("E2E Grain Trader")
                    .email(bEmail)
                    .password("Pass@12345")
                    .phone("9123456781")
                    .role("BUYER")
                    .build();

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bReg)))
                    .andExpect(status().isCreated());

            String bToken = obtainToken(bEmail, "Pass@12345");

            // Buyer searches for "Wheat"
            mockMvc.perform(get("/api/v1/marketplace/listings/search")
                    .header("Authorization", "Bearer " + bToken)
                    .param("query", "Wheat"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))));

            // Step 6: Buyer Places Purchase Order
            ContactSellerRequest buyReq = ContactSellerRequest.builder()
                    .buyerName("E2E Grain Trader")
                    .buyerPhone("9123456781")
                    .buyerEmail(bEmail)
                    .message("Placing purchase order for 200 Quintals of Wheat")
                    .build();

            mockMvc.perform(post("/api/v1/marketplace/listings/" + listingId + "/buy")
                    .header("Authorization", "Bearer " + bToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(buyReq)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.contactStatus").value("PURCHASE_REQUESTED"));
        }
    }
}
