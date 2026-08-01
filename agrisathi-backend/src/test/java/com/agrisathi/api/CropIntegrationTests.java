package com.agrisathi.api;

import com.agrisathi.api.dto.request.CropRequest;
import com.agrisathi.api.dto.request.LoginRequest;
import com.agrisathi.api.dto.request.RegisterRequest;
import com.agrisathi.api.model.enums.CropStatus;
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
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class CropIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        String uniqueEmail = "farmer_crop_" + System.currentTimeMillis() + "@agrisathi.com";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Crop Farmer");
        registerRequest.setEmail(uniqueEmail);
        registerRequest.setPassword("FarmerPass@123");
        registerRequest.setPhone("9876543211");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(uniqueEmail);
        loginRequest.setPassword("FarmerPass@123");

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseStr = loginResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseStr);
        token = jsonNode.get("data").get("accessToken").asText();
    }

    @Test
    void testCropLifecycle() throws Exception {
        // 1. Add crop
        CropRequest addRequest = new CropRequest();
        addRequest.setCropName("Wheat");
        addRequest.setSowingDate(LocalDate.now());
        addRequest.setHarvestDate(LocalDate.now().plusMonths(4));
        addRequest.setStatus(CropStatus.PLANTED);

        MvcResult addResult = mockMvc.perform(post("/api/v1/crops")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cropName").value("Wheat"))
                .andExpect(jsonPath("$.data.status").value("PLANTED"))
                .andReturn();

        JsonNode addedJson = objectMapper.readTree(addResult.getResponse().getContentAsString());
        long cropId = addedJson.get("data").get("id").asLong();

        // 2. List crops
        mockMvc.perform(get("/api/v1/crops")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(cropId));

        // 3. Get crop details
        mockMvc.perform(get("/api/v1/crops/" + cropId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cropName").value("Wheat"));

        // 4. Update crop
        CropRequest updateRequest = new CropRequest();
        updateRequest.setCropName("Wheat - Premium");
        updateRequest.setSowingDate(LocalDate.now());
        updateRequest.setHarvestDate(LocalDate.now().plusMonths(4));
        updateRequest.setStatus(CropStatus.HARVESTED);

        mockMvc.perform(put("/api/v1/crops/" + cropId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cropName").value("Wheat - Premium"))
                .andExpect(jsonPath("$.data.status").value("HARVESTED"));

        // 5. Delete crop
        mockMvc.perform(delete("/api/v1/crops/" + cropId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 6. Verify deletion
        mockMvc.perform(get("/api/v1/crops/" + cropId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
