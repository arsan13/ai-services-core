package com.arsan.ai.provider.chat.registry;

import com.arsan.ai.enums.ChatType;
import com.arsan.ai.provider.chat.core.ChatProvider;
import com.arsan.ai.provider.chat.generic.GenericChatProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatProviderRegistry {

    private final List<ChatProvider> providers;

    public ChatProvider getProvider(ChatType chatType) {
        if (chatType == null) {
            throw new IllegalArgumentException("Chat type must not be null");
        }

        return providers.stream()
                .filter(provider -> provider.getChatType() == chatType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported chat type: " + chatType));
    }
}
