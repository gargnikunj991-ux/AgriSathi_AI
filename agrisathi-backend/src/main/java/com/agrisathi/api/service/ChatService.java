package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.ChatRequest;
import com.agrisathi.api.dto.response.ChatResponse;

import java.util.List;

public interface ChatService {
    ChatResponse processChat(Long userId, ChatRequest request);
    List<ChatResponse> getChatHistory(Long userId);
}
