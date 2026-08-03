package com.agrisathi.api.security;

import com.agrisathi.api.dto.request.CropRequest;
import com.agrisathi.api.dto.request.LoginRequest;
import com.agrisathi.api.dto.request.RegisterRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class RoleBasedSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String farmerToken;
    private String buyerToken;
    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        long timestamp = System.currentTimeMillis();

        // 1. Register & Login FARMER
        String farmerEmail = "farmer_" + timestamp + "@agrisathi.com";
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

        // 2. Register & Login BUYER
        String buyerEmail = "buyer_" + timestamp + "@agrisathi.com";
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

        // 3. Register & Login ADMIN
        String adminEmail = "admin_" + timestamp + "@agrisathi.com";
        RegisterRequest adminReg = RegisterRequest.builder()
                .name("Admin Owner")
                .email(adminEmail)
                .password("AdminPass@123")
                .phone("9876543212")
                .role("ADMIN")
                .build();
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminReg)))
                .andExpect(status().isCreated());
        adminToken = obtainToken(adminEmail, "AdminPass@123");
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

        JsonNode jsonNode = objectMapper.readTree(responseStr);
        return jsonNode.get("data").get("accessToken").asText();
    }

    @Test
    void testStrictPublicEndpoints() throws Exception {
        // Public endpoints (login & register) accessible without token
        RegisterRequest newReg = RegisterRequest.builder()
                .name("New User")
                .email("public_" + System.currentTimeMillis() + "@agrisathi.com")
                .password("UserPass@123")
                .phone("9876543219")
                .role("FARMER")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newReg)))
                .andExpect(status().isCreated());

        // All other endpoints require authentication (401 Unauthorized when unauthenticated)
        mockMvc.perform(get("/api/v1/weather/current"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/marketplace/listings"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/crops"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testFarmerRoleAccess() throws Exception {
        // Farmer can access farming features (Crops)
        CropRequest cropRequest = CropRequest.builder()
                .cropName("Wheat")
                .sowingDate(LocalDate.now().minusDays(10))
                .build();

        mockMvc.perform(post("/api/v1/crops")
                .header("Authorization", "Bearer " + farmerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cropRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        // Farmer cannot access Admin Dashboard -> 403 Forbidden
        mockMvc.perform(get("/api/v1/admin/dashboard")
                .header("Authorization", "Bearer " + farmerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testBuyerRoleAccessAndRestrictions() throws Exception {
        // Buyer CAN view marketplace listings
        mockMvc.perform(get("/api/v1/marketplace/listings")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Buyer CANNOT create crops -> 403 Forbidden
        CropRequest cropRequest = CropRequest.builder()
                .cropName("Rice")
                .sowingDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/api/v1/crops")
                .header("Authorization", "Bearer " + buyerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cropRequest)))
                .andExpect(status().isForbidden());

        // Buyer CANNOT view or create farmer profile -> 403 Forbidden
        mockMvc.perform(get("/api/v1/farmer/profile")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isForbidden());

        // Buyer CANNOT access weather -> 403 Forbidden
        mockMvc.perform(get("/api/v1/weather/current")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isForbidden());

        // Buyer CANNOT access government schemes -> 403 Forbidden
        mockMvc.perform(get("/api/v1/government-schemes")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isForbidden());

        // Buyer CANNOT access chat -> 403 Forbidden
        mockMvc.perform(get("/api/v1/chat/history")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isForbidden());

        // Buyer CANNOT access Admin Dashboard -> 403 Forbidden
        mockMvc.perform(get("/api/v1/admin/dashboard")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAdminRolePrivilegesAndPersonalProfileRestrictions() throws Exception {
        // Admin CAN access Admin Dashboard
        mockMvc.perform(get("/api/v1/admin/dashboard")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("HEALTHY"));

        // Admin CAN access user list
        mockMvc.perform(get("/api/v1/admin/users")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Admin CANNOT access or modify farmer profile / personal information -> 403 Forbidden
        mockMvc.perform(get("/api/v1/farmer/profile")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isForbidden());
    }
}
