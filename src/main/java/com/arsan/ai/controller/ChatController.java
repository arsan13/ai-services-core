package com.arsan.ai.controller;

import com.arsan.ai.exception.custom.AiServiceException;
import com.arsan.ai.model.ai.ChatRequest;
import com.arsan.ai.model.ai.ChatResponse;
import com.arsan.ai.service.AiChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AiChatService aiChatService;

    @PostMapping
    public ChatResponse chat(@RequestBody @Valid ChatRequest chatRequest) throws AiServiceException {
        return aiChatService.generateResponse(chatRequest.getMessage());
    }

    @DeleteMapping("/conversation")
    public void clearConversation() {
        aiChatService.clearConversation();
    }
}

