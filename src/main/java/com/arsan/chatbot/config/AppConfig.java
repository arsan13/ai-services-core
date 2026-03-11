package com.arsan.chatbot.config;

import com.arsan.chatbot.tool.FuelServiceTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
    public ChatClient chatClient(FuelServiceTool fuelServiceTool, ChatClient.Builder builder, ChatMemory chatMemory) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/static/system-prompt.txt");
        String systemPrompt = classPathResource.getContentAsString(StandardCharsets.UTF_8);

        return builder
                .defaultSystem(systemPrompt)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultTools(fuelServiceTool)
                .build();
    }
}
