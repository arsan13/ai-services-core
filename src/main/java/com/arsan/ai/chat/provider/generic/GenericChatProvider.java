package com.arsan.ai.chat.provider.generic;

import com.arsan.ai.chat.enums.ChatType;
import com.arsan.ai.chat.provider.core.AbstractChatProvider;
import com.arsan.ai.core.config.chat.GenericChatConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GenericChatProvider extends AbstractChatProvider {

    public GenericChatProvider(
            @Qualifier(GenericChatConfig.GENERIC_CHAT_CLIENT_BEAN) ChatClient chatClient,
            @Qualifier(GenericChatConfig.GENERIC_CHAT_MEMORY_BEAN) ChatMemory chatMemory) {
        super(chatClient, chatMemory);
    }

    @Override
    public ChatType getChatType() {
        return ChatType.GENERIC;
    }
}
