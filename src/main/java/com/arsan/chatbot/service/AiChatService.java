package com.arsan.chatbot.service;

import com.arsan.chatbot.exception.custom.AiServiceException;
import com.arsan.chatbot.model.ai.ChatResponse;

public interface AiChatService {

    ChatResponse generateResponse(String message, String userId) throws AiServiceException;
}
