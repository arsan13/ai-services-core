package com.arsan.ai.chat.provider.aviation;

import com.arsan.ai.core.config.chat.AviationChatConfig;
import com.arsan.ai.chat.enums.ChatType;
import com.arsan.ai.chat.provider.core.AbstractChatProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AviationChatProvider extends AbstractChatProvider {

    public AviationChatProvider(
            @Qualifier(AviationChatConfig.AVIATION_CHAT_CLIENT_BEAN) ChatClient chatClient,
            @Qualifier(AviationChatConfig.AVIATION_CHAT_MEMORY_BEAN) ChatMemory chatMemory) {
        super(chatClient, chatMemory);
    }

    @Override
    public ChatType getChatType() {
        return ChatType.AVIATION;
    }
}
