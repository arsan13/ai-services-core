package com.arsan.ai.service;

import com.arsan.ai.enums.ChatType;
import com.arsan.ai.exception.custom.AiServiceException;
import com.arsan.ai.model.ai.ChatResponse;
import com.arsan.ai.model.ai.ChatTypeResponse;

import java.util.List;

/**
 * Unified chat service supporting multiple chat types (aviation, generic, etc.).
 * Delegates to appropriate provider based on ChatType.
 * Each provider has its own configuration, system prompt, tools, and conversation memory.
 */
public interface AiChatService {

    List<ChatTypeResponse> getSupportedTypes();

    ChatResponse generateResponse(String message, ChatType chatType) throws AiServiceException;

    void clearConversation(ChatType chatType);
}
