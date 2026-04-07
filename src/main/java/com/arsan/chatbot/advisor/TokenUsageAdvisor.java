package com.arsan.chatbot.advisor;

import com.arsan.chatbot.entity.TokenUsageAudit;
import com.arsan.chatbot.service.TokenUsageAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUsageAdvisor implements CallAdvisor {

    private final TokenUsageAuditService tokenUsageAuditService;

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        long startTime = System.currentTimeMillis();
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        long latencyMs = (System.currentTimeMillis() - startTime);

        TokenUsageAudit auditedTokenUsage = tokenUsageAuditService.auditTokenUsage(chatClientRequest, chatClientResponse, latencyMs);
        log.info("Prompt: {}", auditedTokenUsage);

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "token-usage-advisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
