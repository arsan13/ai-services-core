package com.arsan.ai.service.impl;

import com.arsan.ai.entity.TokenUsageAudit;
import com.arsan.ai.entity.User;
import com.arsan.ai.model.common.DateRange;
import com.arsan.ai.projection.TokenUsageAuditView;
import com.arsan.ai.projection.UserTokenUsage;
import com.arsan.ai.repository.TokenUsageAuditRepository;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.service.TokenUsageAuditService;
import com.arsan.ai.util.OpenAiCostCalculator;
import com.arsan.ai.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenUsageAuditServiceImpl implements TokenUsageAuditService {

    private final UserRepository userRepository;
    private final TokenUsageAuditRepository auditRepository;

    @Override
    public List<TokenUsageAuditView> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending().and(Sort.by("id").descending()));
        return auditRepository.findAllBy(pageable);
    }

    @Override
    public List<TokenUsageAuditView> getByUserId(Long userId) {
        return auditRepository.findByUserId(userId);
    }

    @Override
    public List<TokenUsageAuditView> getAuditsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        DateRange range = DateRange.resolve(startDate, endDate);
        return auditRepository.findByCreatedDateBetween(range.start(), range.end());
    }

    @Override
    public Long getTotalTokens(LocalDateTime startDate, LocalDateTime endDate) {
        DateRange range = DateRange.resolve(startDate, endDate);
        return auditRepository.sumTotalTokensByCreatedDateBetween(range.start(), range.end());
    }

    @Override
    public Long getTotalTokensByUser(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        DateRange range = DateRange.resolve(startDate, endDate);
        User user = userRepository.getReferenceById(userId);
        return auditRepository.sumTotalTokensByUserAndCreatedDateBetween(user, range.start(), range.end());
    }

    @Override
    public List<UserTokenUsage> getUserTokenUsageSummary(LocalDateTime startDate, LocalDateTime endDate) {
        DateRange range = DateRange.resolve(startDate, endDate);
        return auditRepository.findUserTokenUsageByCreatedDateBetween(range.start(), range.end());
    }

    @Override
    @Transactional
    public TokenUsageAudit recordUsage(ChatClientRequest chatClientRequest, ChatClientResponse chatClientResponse, long latencyMs) {
        TokenUsageAudit audit = new TokenUsageAudit();
        audit.setUser(SecurityUtils.getCurrentUser().orElse(null));
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

        return auditRepository.save(audit);
    }
}
