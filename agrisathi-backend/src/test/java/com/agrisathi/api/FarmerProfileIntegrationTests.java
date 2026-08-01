package com.agrisathi.api;

import com.agrisathi.api.dto.request.FarmerProfileRequest;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.repository.FarmerProfileRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.security.JwtTokenProvider;
import com.agrisathi.api.security.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class FarmerProfileIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerProfileRepository farmerProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        String uniqueEmail = "profileuser_" + System.currentTimeMillis() + "@agrisathi.com";
        User user = User.builder()
                .name("Profile Test User")
                .email(uniqueEmail)
                .passwordHash(passwordEncoder.encode("Password@123"))
                .phone("9876543210")
                .build();
        User savedUser = userRepository.save(user);
        UserPrincipal userPrincipal = UserPrincipal.create(savedUser);
        jwtToken = jwtTokenProvider.generateTokenFromUserPrincipal(userPrincipal);
    }

    @Test
    void testFarmerProfileCrudFlow() throws Exception {
        FarmerProfileRequest request = new FarmerProfileRequest();
        request.setState("Uttarakhand");
        request.setDistrict("Dehradun");
        request.setVillage("Raipur");
        request.setFarmSize(new BigDecimal("3.5"));
        request.setSoilType("Loamy");
        request.setMainCrop("Rice");

        // 1. Create Profile (POST)
        mockMvc.perform(post("/api/v1/farmer/profile")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.state").value("Uttarakhand"));

        // 2. Get Profile (GET)
        mockMvc.perform(get("/api/v1/farmer/profile")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.district").value("Dehradun"));

        // 3. Update Profile (PUT)
        request.setVillage("Doiwala");
        mockMvc.perform(put("/api/v1/farmer/profile")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.village").value("Doiwala"));

        // 4. Delete Profile (DELETE)
        mockMvc.perform(delete("/api/v1/farmer/profile")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile Deleted Successfully"));
    }
}
