package com.arsan.ai.chat.provider.registry;

import com.arsan.ai.chat.enums.ChatType;
import com.arsan.ai.chat.provider.core.ChatProvider;
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
