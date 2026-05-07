package com.arsan.ai.advisor;

import com.arsan.ai.entity.AppUser;
import com.arsan.ai.service.audit.TokenUsageAuditService;
import com.arsan.ai.util.SecurityUtils;
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

        AppUser user = SecurityUtils.getCurrentUser().orElse(null);
        tokenUsageAuditService.recordUsage(user, chatClientRequest, chatClientResponse, latencyMs);

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
