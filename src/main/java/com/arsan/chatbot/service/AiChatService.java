package com.arsan.chatbot.service;

import com.arsan.chatbot.model.ChatResponse;
import com.arsan.chatbot.exception.custom.AiServiceException;

public interface AiChatService {

    ChatResponse generateResponse(String message, String userId) throws AiServiceException;
}
