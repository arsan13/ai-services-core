package com.arsan.ai.chat.service.impl;

import com.arsan.ai.chat.enums.ChatType;
import com.arsan.ai.chat.model.ChatResponse;
import com.arsan.ai.chat.model.ChatTypeResponse;
import com.arsan.ai.chat.provider.core.ChatProvider;
import com.arsan.ai.chat.provider.registry.ChatProviderRegistry;
import com.arsan.ai.chat.service.AiChatService;
import com.arsan.ai.core.exception.custom.AiServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatProviderRegistry chatProviderRegistry;

    @Override
    public List<ChatTypeResponse> getSupportedTypes() {
        return Arrays.stream(ChatType.values())
                .map(type -> new ChatTypeResponse(type.getCode(), type.getDisplayName(), type.getPermission()))
                .toList();
    }

    @Override
    public ChatResponse generateResponse(String message, ChatType chatType) throws AiServiceException {
        ChatProvider provider = chatProviderRegistry.getProvider(chatType);
        return provider.generateResponse(message);
    }

    @Override
    public void clearConversation(ChatType chatType) {
        ChatProvider provider = chatProviderRegistry.getProvider(chatType);
        provider.clearConversation();
    }
}
