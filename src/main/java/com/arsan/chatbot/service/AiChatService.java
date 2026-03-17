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

        return outputText;
    }

    public List<Message> getChatHistory() {
        return chatMemory.get("default");
    }

    private String loadContext() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/static/context-V1.txt");
        return classPathResource.getContentAsString(StandardCharsets.UTF_8);
    }
}
