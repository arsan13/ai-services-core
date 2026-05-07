package com.arsan.ai.chat.provider.core;

import com.arsan.ai.chat.enums.ChatType;
import com.arsan.ai.core.exception.custom.AiServiceException;
import com.arsan.ai.chat.model.ChatResponse;

public interface ChatProvider {

    ChatResponse generateResponse(String message) throws AiServiceException;

    void clearConversation();

    ChatType getChatType();
}
