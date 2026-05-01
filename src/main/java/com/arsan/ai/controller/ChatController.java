package com.arsan.ai.controller;

import com.arsan.ai.enums.ChatType;
import com.arsan.ai.exception.custom.AiServiceException;
import com.arsan.ai.model.ai.ChatRequest;
import com.arsan.ai.model.ai.ChatResponse;
import com.arsan.ai.model.ai.ChatTypeResponse;
import com.arsan.ai.service.AiChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AiChatService aiChatService;

    @GetMapping("/types")
    public List<ChatTypeResponse> getSupportedTypes() {
        return aiChatService.getSupportedTypes();
    }

    @PreAuthorize("@chatPermissionEvaluator.hasAccess(authentication, #chatType)")
    @PostMapping("/{chatType}")
    public ChatResponse chat(
            @RequestBody @Valid ChatRequest chatRequest,
            @PathVariable String chatType) throws AiServiceException {

        ChatType type = ChatType.fromCode(chatType);
        return aiChatService.generateResponse(chatRequest.getMessage(), type);
    }

    @PreAuthorize("@chatPermissionEvaluator.hasAccess(authentication, #chatType)")
    @DeleteMapping("/{chatType}/conversation")
    public void clearConversation(@PathVariable String chatType) {

        ChatType type = ChatType.fromCode(chatType);
        aiChatService.clearConversation(type);
    }
}

