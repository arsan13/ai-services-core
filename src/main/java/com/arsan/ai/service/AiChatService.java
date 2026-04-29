package com.arsan.ai.service;

import com.arsan.ai.exception.custom.AiServiceException;
import com.arsan.ai.model.ai.ChatResponse;

public interface AiChatService {

    ChatResponse generateResponse(String message) throws AiServiceException;
}
