package com.arsan.ai.core.config.chat;

import com.arsan.ai.chat.enums.AiModelProvider;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ChatModelResolver {

    private final ChatModel openAiChatModel;
    private final ChatModel anthropicChatModel;

    public ChatModelResolver(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel,
            @Qualifier("anthropicChatModel") ChatModel anthropicChatModel) {
        this.openAiChatModel = openAiChatModel;
        this.anthropicChatModel = anthropicChatModel;
    }

    public ChatModel resolve(AiModelProvider provider) {
        return switch (provider) {
            case OPENAI -> openAiChatModel;
            case ANTHROPIC -> anthropicChatModel;
        };
    }
}
