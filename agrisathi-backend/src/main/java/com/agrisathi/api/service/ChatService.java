package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.ChatRequest;
import com.agrisathi.api.dto.response.ChatResponse;

public interface ChatService {
    ChatResponse processChat(Long userId, ChatRequest request);
}
