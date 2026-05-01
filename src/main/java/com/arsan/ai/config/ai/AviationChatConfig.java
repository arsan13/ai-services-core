package com.arsan.ai.config.ai;

import com.arsan.ai.advisor.TokenUsageAdvisor;
import com.arsan.ai.tool.FuelServiceTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AviationChatConfig {

    public static final String AVIATION_CHAT_MEMORY_BEAN = "aviationChatMemory";
    public static final String AVIATION_CHAT_CLIENT_BEAN = "aviationChatClient";

    @Value("classpath:/prompts/system-prompt.st")
    private Resource systemPrompt;

    @Bean(name = AVIATION_CHAT_MEMORY_BEAN)
    public ChatMemory aviationChatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
    }

    @Bean(name = AVIATION_CHAT_CLIENT_BEAN)
    public ChatClient aviationChatClient(FuelServiceTool fuelServiceTool, ChatClient.Builder builder, ChatMemory aviationChatMemory, TokenUsageAdvisor tokenUsageAdvisor) {
        return builder
                .defaultSystem(systemPrompt)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(aviationChatMemory).build(),
                        tokenUsageAdvisor
                )
                .defaultTools(fuelServiceTool)
                .build();
    }
}
