package com.arsan.ai.provider.chat.core;

import com.arsan.ai.enums.ChatType;
import com.arsan.ai.exception.custom.AiServiceException;
import com.arsan.ai.model.ai.ChatResponse;
import com.arsan.ai.util.AiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractChatProvider implements ChatProvider {

    protected final ChatClient chatClient;
    protected final ChatMemory chatMemory;

    @Override
    public ChatResponse generateResponse(String message) throws AiServiceException {
        String conversationId = AiUtils.getConversationId();
        try {
            log.info("{} - User message [ConversationId: {}]", getChatType().getDisplayName(), conversationId);

            String outputText = chatClient
                    .prompt()
                    .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, conversationId))
                    .user(message)
                    .call()
                    .content();

            return new ChatResponse(outputText);
        } catch (Exception e) {
            log.error("{} response failed [ConversationId: {}]", getChatType().getDisplayName(), conversationId, e);
            throw new AiServiceException("Failed to generate response: " + e.getMessage());
        }
    }

    @Override
    public void clearConversation() {
        String conversationId = AiUtils.getConversationId();
        try {
            chatMemory.clear(conversationId);
            log.info("{} conversation cleared [ConversationId: {}]", getChatType().getDisplayName(), conversationId);
        } catch (Exception e) {
            log.warn("Failed to clear {} conversation [ConversationId: {}]", getChatType().getDisplayName(), conversationId, e);
        }
    }
}
