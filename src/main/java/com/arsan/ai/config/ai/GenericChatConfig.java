package com.arsan.ai.config.ai;

import com.arsan.ai.advisor.TokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenericChatConfig {

    public static final String GENERIC_CHAT_MEMORY_BEAN = "genericChatMemory";
    public static final String GENERIC_CHAT_CLIENT_BEAN = "genericChatClient";

    @Bean(name = GENERIC_CHAT_MEMORY_BEAN)
    public ChatMemory genericChatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
    }

    @Bean(name = GENERIC_CHAT_CLIENT_BEAN)
    public ChatClient genericChatClient(ChatClient.Builder builder, ChatMemory genericChatMemory, TokenUsageAdvisor tokenUsageAdvisor) {
        return builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(genericChatMemory).build(),
                        tokenUsageAdvisor
                )
                .build();
    }
}
