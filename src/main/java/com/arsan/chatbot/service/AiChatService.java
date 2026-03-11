package com.arsan.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public String generateResponse(String message) throws IOException {
        if (isGreeting(message)) {
            return "Hello! Please provide the aircraft registration number so I can analyze the fuel report.";
        }
        log.info("User sent message: {}", message);

        ClassPathResource classPathResource = new ClassPathResource("/static/context.txt");
        String contextTxt = classPathResource.getContentAsString(StandardCharsets.UTF_8);
        PromptTemplate promptTemplate = new PromptTemplate(contextTxt);
        Prompt prompt = promptTemplate.create(Map.of("userMessage", message));

        String outputText = chatClient
                .prompt(prompt)
                .call()
                .content();

        chatMemory.get("default").forEach(m ->
                log.debug("{}: {}", m.getMessageType(), m.getText())
        );

        log.info("Model responded with: {}", outputText);
        return outputText;
    }

    private boolean isGreeting(String msg) {
        String lower = msg.toLowerCase().trim();
        return lower.matches("^(hi|hello|hey|good morning|good afternoon|good evening)\\b.*");
    }
}
