package com.arsan.ai.provider.chat.core;

import com.arsan.ai.enums.ChatType;
import com.arsan.ai.exception.custom.AiServiceException;
import com.arsan.ai.model.ai.ChatResponse;

public interface ChatProvider {

    ChatResponse generateResponse(String message) throws AiServiceException;

    void clearConversation();

    ChatType getChatType();
}
