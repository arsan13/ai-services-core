package com.arsan.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public String generateResponse(String message, String userId) throws Exception {
        log.info("User sent message: {}", message);

        String outputText = chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, userId))
                .user(message)
                .call()
                .content();

        log.info("Model responded with: {}", outputText);

        return Optional.ofNullable(outputText).orElse("Sorry, I couldn't generate a response.");
    }

    public List<Message> getChatHistory(String userId) {
        return chatMemory.get(userId);
    }
}
