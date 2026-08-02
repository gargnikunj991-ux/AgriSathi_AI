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

import java.util.List;
import java.util.stream.Collectors;

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

        String prompt = request.getMessage() != null ? request.getMessage().trim() : "";
        String aiResponse = generateMockAiResponse(prompt);

        ChatHistory history = ChatHistory.builder()
                .user(user)
                .prompt(prompt)
                .response(aiResponse)
                .build();

        ChatHistory savedHistory = chatHistoryRepository.save(history);

        return ChatResponse.builder()
                .id(savedHistory.getId())
                .prompt(savedHistory.getPrompt())
                .response(savedHistory.getResponse())
                .timestamp(savedHistory.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatResponse> getChatHistory(Long userId) {
        return chatHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(history -> ChatResponse.builder()
                        .id(history.getId())
                        .prompt(history.getPrompt())
                        .response(history.getResponse())
                        .timestamp(history.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private String generateMockAiResponse(String prompt) {
        String lower = prompt.toLowerCase();

        if (lower.contains("rust") || lower.contains("blight") || lower.contains("disease") || lower.contains("spot")) {
            return "For fungal diseases like Leaf Rust or Blight, inspect leaves for yellow/brown pustules. Spray Copper Oxychloride (2.5g/L) or Mancozeb (2g/L) at 10-day intervals. Avoid overhead irrigation to keep foliage dry.";
        } else if (lower.contains("fertilizer") || lower.contains("npk") || lower.contains("dap") || lower.contains("urea")) {
            return "Balanced fertilization is key: apply basal NPK (19:19:19) at sowing, followed by top dressing of Urea (45kg/acre) during early vegetative growth. Supplement with organic vermicompost to boost soil microbiota.";
        } else if (lower.contains("weather") || lower.contains("rain") || lower.contains("temperature")) {
            return "Always monitor local weather advisories before applying pesticides or fertilizers. Avoid spraying during high winds or immediately before forecasted heavy rainfall.";
        } else if (lower.contains("scheme") || lower.contains("subsidy") || lower.contains("loan") || lower.contains("pm-kisan")) {
            return "Check the Government Schemes tab in AgriSathi! Major active schemes include PM-KISAN (₹6000 annual income support), PM Fasal Bima Yojana (crop insurance), and Sub-Mission on Agricultural Mechanization.";
        } else if (lower.contains("price") || lower.contains("market") || lower.contains("sell") || lower.contains("mandi")) {
            return "You can list your produce directly in the AgriSathi Produce Marketplace to reach buyers with zero middleman commissions. Ensure produce is graded and clean for best market prices.";
        } else if (lower.contains("water") || lower.contains("drip") || lower.contains("irrigation")) {
            return "Drip irrigation can save up to 40-50% water while increasing yield by 20%. Ensure filters are cleaned regularly to prevent micro-emitter clogging.";
        } else {
            return "Hello! I am your AgriSathi AI Agricultural Assistant. You can ask me questions about crop disease diagnosis, fertilizer dosages, weather advisories, market prices, or government welfare schemes!";
        }
    }
}
