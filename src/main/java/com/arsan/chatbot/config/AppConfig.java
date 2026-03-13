package com.arsan.chatbot.config;

import com.arsan.chatbot.tool.FuelServiceTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
    }

    @Bean
    public ChatClient chatClient(FuelServiceTool fuelServiceTool, ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultSystem(loadSystemPrompt())
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultTools(fuelServiceTool)
                .build();
    }

    private String loadSystemPrompt() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("/static/system-prompt.txt");
            return StreamUtils.copyToString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to load system prompt: " + e.getMessage());
            return """
                    You are an AI assistant specialized in Aviation fuel operations analysis.
                    Always follow the fuel discrepancy workflow.
                    """;
        }
    }
}
