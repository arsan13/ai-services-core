package com.arsan.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public String generateResponse(String message) throws IOException {
        log.info("User sent message: {}", message);

        String outputText = chatClient
                .prompt()
                .system(loadContext())
                .user(message)
                .call()
                .content();

        log.info("Model responded with: {}", outputText);

        return outputText;
    }

    private String loadContext() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/static/context.txt");
        return classPathResource.getContentAsString(StandardCharsets.UTF_8);
    }

    private boolean isGreeting(String msg) {
        String lower = msg.toLowerCase().trim();
        return lower.matches("^(hi|hello|hey|good morning|good afternoon|good evening)\\b.*");
    }
}
