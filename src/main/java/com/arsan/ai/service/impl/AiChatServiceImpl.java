package com.arsan.ai.service.impl;

import com.arsan.ai.exception.custom.AiServiceException;
import com.arsan.ai.model.ai.ChatResponse;
import com.arsan.ai.service.AiChatService;
import com.arsan.ai.util.AiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public ChatResponse generateResponse(String message) throws AiServiceException {
        try {
            log.info("User sent message: {}", message);

            String outputText = chatClient
                    .prompt()
                    .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, AiUtils.getConversationId()))
                    .user(message)
                    .call()
                    .content();

            log.info("Model responded with: {}", outputText);

            return new ChatResponse(outputText);
        } catch (Exception e) {
            throw new AiServiceException("Failed to generate response. " + e.getMessage());
        }
    }

    @Override
    public void clearConversation() {
        chatMemory.clear(String.valueOf(AiUtils.getConversationId()));
    }
}
