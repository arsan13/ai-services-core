package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.entity.TokenUsageAudit;
import com.arsan.chatbot.model.DateRange;
import com.arsan.chatbot.projection.UserTokenUsage;
import com.arsan.chatbot.repository.TokenUsageAuditRepository;
import com.arsan.chatbot.service.TokenUsageAuditService;
import com.arsan.chatbot.util.OpenAiCostCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenUsageAuditServiceImpl implements TokenUsageAuditService {

    private final TokenUsageAuditRepository repository;

    @Override
    public List<TokenUsageAudit> getAll() {
        return repository.findAll();
    }

    @Override
    public List<TokenUsageAudit> getByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<TokenUsageAudit> getAuditsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        DateRange range = DateRange.resolve(startDate, endDate);
        return repository.findByCreatedDateBetween(range.start(), range.end());
    }

    @Override
    public Long getTotalTokens(LocalDateTime startDate, LocalDateTime endDate) {
        DateRange range = DateRange.resolve(startDate, endDate);
        return repository.sumTotalTokensByCreatedDateBetween(range.start(), range.end());
    }

    @Override
    public Long getTotalTokensByUser(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        DateRange range = DateRange.resolve(startDate, endDate);
        return repository.sumTotalTokensByUserIdAndCreatedDateBetween(userId, range.start(), range.end());
    }

    @Override
    public List<UserTokenUsage> getUserTokenUsageSummary(LocalDateTime startDate, LocalDateTime endDate) {
        DateRange range = DateRange.resolve(startDate, endDate);
        return repository.findUserTokenUsageByCreatedDateBetween(range.start(), range.end());
    }

    @Override
    public TokenUsageAudit recordUsage(ChatClientRequest chatClientRequest, ChatClientResponse chatClientResponse, long latencyMs) {
        TokenUsageAudit audit = new TokenUsageAudit();
        audit.setUserId("default-user");
        audit.setProvider("openai");
        audit.setLatencySec(TimeUnit.MILLISECONDS.toSeconds(latencyMs));

        audit.setInputSummary(chatClientRequest.prompt().getUserMessage().getText());

        ChatResponse chatResponse = chatClientResponse.chatResponse();
        if (chatResponse != null) {
            audit.setModel(chatResponse.getMetadata().getModel());

            Usage usage = chatResponse.getMetadata().getUsage();
            audit.setPromptTokens(usage.getPromptTokens());
            audit.setCompletionTokens(usage.getCompletionTokens());
            audit.setTotalTokens(usage.getTotalTokens());

            double cost = OpenAiCostCalculator.calculateCost(audit.getModel(), audit.getPromptTokens(), audit.getCompletionTokens());
            audit.setCostInUsd(cost);

            audit.setOutputSummary(chatResponse.getResult().getOutput().getText());
        }

        return repository.save(audit);
    }
}
