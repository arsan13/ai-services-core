package com.arsan.chatbot.config;

import com.arsan.chatbot.advisor.TokenUsageAdvisor;
import com.arsan.chatbot.tool.FuelServiceToolV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@Configuration
public class AppConfig {

    @Value("classpath:/prompts/system-prompt.st")
    private Resource systemPrompt;

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
    }

    @Bean
    public ChatClient chatClient(FuelServiceToolV1 fuelServiceTool, ChatClient.Builder builder, ChatMemory chatMemory, TokenUsageAdvisor tokenUsageAdvisor) {
        return builder
                .defaultSystem(systemPrompt)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        tokenUsageAdvisor
                )
                .defaultTools(fuelServiceTool)
                .build();
    }
}
