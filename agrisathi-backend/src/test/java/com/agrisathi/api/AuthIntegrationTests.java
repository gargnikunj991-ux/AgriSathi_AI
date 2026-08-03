package com.agrisathi.api;

import com.agrisathi.api.dto.request.LoginRequest;
import com.agrisathi.api.dto.request.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class AuthIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegistrationMissingRoleFails() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("No Role User");
        registerRequest.setEmail("norole_" + System.currentTimeMillis() + "@agrisathi.com");
        registerRequest.setPassword("FarmerPass@123");
        registerRequest.setPhone("9876543210");
        // Role is omitted

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterAndLoginFlowWithFixedRole() throws Exception {
        String uniqueEmail = "farmer_" + System.currentTimeMillis() + "@agrisathi.com";

        // 1. Register User with mandatory FARMER role
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Test Farmer");
        registerRequest.setEmail(uniqueEmail);
        registerRequest.setPassword("FarmerPass@123");
        registerRequest.setPhone("9876543210");
        registerRequest.setRole("FARMER");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        // 2. Login User
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(uniqueEmail);
        loginRequest.setPassword("FarmerPass@123");

        String responseStr = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.expiresIn").value(1800))
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(responseStr).get("data").get("accessToken").asText();

        // 3. Verify user details return fixed role ROLE_FARMER
        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.role").value("ROLE_FARMER"));
    }
}
