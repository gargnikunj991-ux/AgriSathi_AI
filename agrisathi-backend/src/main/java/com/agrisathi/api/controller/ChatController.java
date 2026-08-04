package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.ChatRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.ChatResponse;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "AI Chat APIs", description = "Endpoints for interactive AI agricultural advisory assistant and chat conversation history")
@SecurityRequirement(name = "bearerAuth")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    @Operation(summary = "Chat with AgriSathi AI", description = "Sends a farmer question or query to AgriSathi AI assistant for intelligent agricultural guidance.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "AI Chat response generated",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Blank or invalid chat query")
    })
    public ResponseEntity<ApiResponse<ChatResponse>> processChat(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatService.processChat(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("AI Chat response generated", response));
    }

    @GetMapping("/history")
    @Operation(summary = "Get Chat History", description = "Retrieves conversation transcript history between the authenticated user and AgriSathi AI assistant.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Chat history retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ChatResponse>>> getChatHistory(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<ChatResponse> history = chatService.getChatHistory(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Chat history retrieved successfully", history));
    }
}
