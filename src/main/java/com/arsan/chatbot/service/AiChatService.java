package com.arsan.chatbot.service;

import com.arsan.chatbot.exception.custom.AiServiceException;
import com.arsan.chatbot.model.ChatResponse;

public interface AiChatService {

    ChatResponse generateResponse(String message, String userId) throws AiServiceException;
}
