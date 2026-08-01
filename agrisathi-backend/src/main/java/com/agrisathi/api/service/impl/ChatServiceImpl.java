package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.ChatRequest;
import com.agrisathi.api.dto.response.ChatResponse;
import com.agrisathi.api.exception.ResourceNotFoundException;
import com.agrisathi.api.model.entity.ChatHistory;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.repository.ChatHistoryRepository;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatResponse processChat(Long userId, ChatRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        String prompt = request.getMessage();
        String aiResponse = "Leaf rust can be treated using copper fungicides. Ensure adequate field ventilation, avoid overhead watering, and practice crop rotation.";

        ChatHistory history = ChatHistory.builder()
                .user(user)
                .prompt(prompt)
                .response(aiResponse)
                .build();

        chatHistoryRepository.save(history);

        return ChatResponse.builder()
                .response(aiResponse)
                .build();
    }
}
