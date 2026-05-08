package com.arsan.ai.chat.provider.core;

import com.arsan.ai.chat.enums.ChatType;
import com.arsan.ai.chat.model.ChatResponse;
import com.arsan.ai.core.exception.custom.AiServiceException;

public interface ChatProvider {

    ChatResponse generateResponse(String message) throws AiServiceException;

    void clearConversation();

    ChatType getChatType();
}
