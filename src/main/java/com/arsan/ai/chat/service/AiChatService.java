package com.arsan.ai.chat.service;

import com.arsan.ai.chat.enums.ChatType;
import com.arsan.ai.chat.model.ChatResponse;
import com.arsan.ai.chat.model.ChatTypeResponse;
import com.arsan.ai.core.exception.custom.AiServiceException;

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
