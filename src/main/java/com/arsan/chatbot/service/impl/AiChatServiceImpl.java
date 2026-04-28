package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.exception.custom.AiServiceException;
import com.arsan.chatbot.model.ai.ChatResponse;
import com.arsan.chatbot.service.AiChatService;
import com.arsan.chatbot.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;

    public ChatResponse generateResponse(String message) throws AiServiceException {
        try {
            log.info("User sent message: {}", message);

            Long userId = SecurityUtils.getCurrentUserId().orElse(0L);
            String outputText = chatClient
                    .prompt()
                    .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, userId))
                    .user(message)
                    .call()
                    .content();

            log.info("Model responded with: {}", outputText);

            return new ChatResponse(outputText);
        } catch (Exception e) {
            throw new AiServiceException("Failed to generate response. " + e.getMessage());
        }
    }
}
