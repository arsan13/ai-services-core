package com.arsan.ai.service.impl;

import com.arsan.ai.enums.ChatType;
import com.arsan.ai.exception.custom.AiServiceException;
import com.arsan.ai.model.ai.ChatResponse;
import com.arsan.ai.model.ai.ChatTypeResponse;
import com.arsan.ai.provider.chat.core.ChatProvider;
import com.arsan.ai.provider.chat.registry.ChatProviderRegistry;
import com.arsan.ai.service.AiChatService;
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
                .map(type -> new ChatTypeResponse(type.getCode(), type.getDisplayName()))
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
