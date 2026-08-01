package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.ChatRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.ChatResponse;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> processChat(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatService.processChat(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("AI Chat response generated", response));
    }
}
