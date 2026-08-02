package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.ChatRequest;
import com.agrisathi.api.dto.response.ChatResponse;
import com.agrisathi.api.model.entity.ChatHistory;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.repository.ChatHistoryRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.impl.ChatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatHistoryRepository chatHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .name("Test Farmer")
                .email("farmer@agrisathi.com")
                .passwordHash("EncodedPass")
                .phone("9876543210")
                .build();
    }

    @Test
    void testProcessChat_FungalDiseasePrompt() {
        ChatRequest request = new ChatRequest();
        request.setMessage("How do I treat leaf rust on my wheat crop?");

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(chatHistoryRepository.save(any())).thenAnswer(invocation -> {
            ChatHistory entity = invocation.getArgument(0);
            entity.setId(101L);
            return entity;
        });

        ChatResponse response = chatService.processChat(1L, request);

        assertNotNull(response);
        assertEquals("How do I treat leaf rust on my wheat crop?", response.getPrompt());
        assertTrue(response.getResponse().contains("fungicides") || response.getResponse().contains("Copper"));
    }

    @Test
    void testGetChatHistory_Success() {
        ChatHistory historyItem = ChatHistory.builder()
                .id(101L)
                .user(sampleUser)
                .prompt("What is NPK?")
                .response("NPK stands for Nitrogen, Phosphorus, and Potassium.")
                .build();

        when(chatHistoryRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(Collections.singletonList(historyItem));

        List<ChatResponse> historyList = chatService.getChatHistory(1L);

        assertNotNull(historyList);
        assertEquals(1, historyList.size());
        assertEquals("What is NPK?", historyList.get(0).getPrompt());
    }
}
