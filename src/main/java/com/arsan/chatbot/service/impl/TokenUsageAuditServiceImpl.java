package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.entity.TokenUsageAudit;
import com.arsan.chatbot.proection.UserTokenUsage;
import com.arsan.chatbot.repository.TokenUsageAuditRepository;
import com.arsan.chatbot.service.TokenUsageAuditService;
import com.arsan.chatbot.util.OpenAiCostCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenUsageAuditServiceImpl implements TokenUsageAuditService {

    private final TokenUsageAuditRepository tokenUsageAuditRepository;

    @Override
    public List<TokenUsageAudit> findAll() {
        return tokenUsageAuditRepository.findAll();
    }

    @Override
    public List<TokenUsageAudit> findByUserId(String userId) {
        return tokenUsageAuditRepository.findByUserId(userId);
    }

    @Override
    public List<TokenUsageAudit> getTodayAudits() {
        LocalDate today = LocalDate.now();
        return getAuditsByDateRange(today.atStartOfDay(), today.atTime(LocalTime.MAX));
    }

    @Override
    public List<TokenUsageAudit> getAuditsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return tokenUsageAuditRepository.findByCreatedDateBetween(startDate, endDate);
    }

    @Override
    public Long getTodayTotalTokens() {
        LocalDate today = LocalDate.now();
        return getTotalTokensByDateRange(today.atStartOfDay(), today.atTime(LocalTime.MAX));
    }

    @Override
    public Long getTotalTokensByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return tokenUsageAuditRepository.getTotalTokensUsedBetween(startDate, endDate);
    }

    public Long getTodayUsageByUser(Long userId) {
        LocalDate today = LocalDate.now();
        return tokenUsageAuditRepository.getUserTotalTokensBetween(userId, today.atStartOfDay(), today.atTime(LocalTime.MAX));
    }

    public List<UserTokenUsage> getTodayUsageOfAllUsers() {
        LocalDate today = LocalDate.now();
        return tokenUsageAuditRepository.getUserTokenUsageBetween(today.atStartOfDay(), today.atTime(LocalTime.MAX));
    }

    @Override
    public TokenUsageAudit auditTokenUsage(ChatClientRequest chatClientRequest, ChatClientResponse chatClientResponse, long latencyMs) {
        TokenUsageAudit audit = new TokenUsageAudit();
        audit.setUserId("default-user");
        audit.setProvider("openai");
        audit.setLatencySec(TimeUnit.MILLISECONDS.toSeconds(latencyMs));

        ChatResponse chatResponse = chatClientResponse.chatResponse();
        if (chatResponse != null) {
            audit.setModel(chatResponse.getMetadata().getModel());

            Usage usage = chatResponse.getMetadata().getUsage();
            audit.setPromptTokens(usage.getPromptTokens());
            audit.setCompletionTokens(usage.getCompletionTokens());
            audit.setTotalTokens(usage.getTotalTokens());

            double cost = OpenAiCostCalculator.calculateCost(audit.getModel(), audit.getPromptTokens(), audit.getCompletionTokens());
            audit.setCostInUSD(cost);
        }

        audit.setInputSummary(chatClientRequest.prompt().getUserMessage().getText());
        if (chatResponse != null) {
            audit.setOutputSummary(chatResponse.getResult().getOutput().getText());
        }

        return tokenUsageAuditRepository.save(audit);
    }
}
