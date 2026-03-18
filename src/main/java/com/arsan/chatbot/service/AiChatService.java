package com.arsan.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public String generateResponse(String message) throws Exception {
        log.info("User sent message: {}", message);

        String outputText = chatClient
                .prompt()
                .system(loadContext())
                .user(message)
                .call()
                .content();

        log.info("Model responded with: {}", outputText);

        return Optional.ofNullable(outputText).orElse("Sorry, I couldn't generate a response.");
    }

    public List<Message> getChatHistory() {
        return chatMemory.get("default");
    }

    private String loadContext() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("/static/context-v1.txt");
            return classPathResource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to load workflow context: {}", e.getMessage());
            return "";
        }
    }
}
