package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.dto.ChatResponse;
import com.arsan.chatbot.exception.custom.AiServiceException;
import com.arsan.chatbot.service.AiChatService;
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

    public ChatResponse generateResponse(String message, String userId) throws AiServiceException {
        log.info("User sent message: {}", message);

        String outputText = chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, userId))
                .user(message)
                .call()
                .content();

        log.info("Model responded with: {}", outputText);

        return new ChatResponse(outputText);
    }
}
