package com.arsan.chatbot.controller;

import com.arsan.chatbot.exception.custom.AiServiceException;
import com.arsan.chatbot.model.common.ApiResponse;
import com.arsan.chatbot.model.ai.ChatRequest;
import com.arsan.chatbot.model.ai.ChatResponse;
import com.arsan.chatbot.service.AiChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.chat.memory.ChatMemory.DEFAULT_CONVERSATION_ID;

@RestController
@RequestMapping("/ai/chat")
@CrossOrigin
@RequiredArgsConstructor
public class ChatController {

    private final AiChatService aiChatService;

    @PostMapping
    public ApiResponse<ChatResponse> chat(
            @RequestHeader(value = "userId", required = false, defaultValue = DEFAULT_CONVERSATION_ID) String userId,
            @RequestBody @Valid ChatRequest chatRequest) throws AiServiceException {
        ChatResponse response = aiChatService.generateResponse(chatRequest.getMessage(), userId);
        return ApiResponse.success(response);
    }
}

