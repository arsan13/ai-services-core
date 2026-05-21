package com.arsan.ai.admin.service;

import com.arsan.ai.admin.repository.projection.TokenUsageAuditView;
import com.arsan.ai.admin.repository.projection.UserTokenUsage;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenUsageAuditService {

    List<TokenUsageAuditView> getAll(Pageable pageable);

    List<TokenUsageAuditView> getByUserId(Long userId);

    List<TokenUsageAuditView> getAuditsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Long getTotalTokens(LocalDateTime startDate, LocalDateTime endDate);

    Long getTotalTokensByUser(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<UserTokenUsage> getUserTokenUsageSummary(LocalDateTime startDate, LocalDateTime endDate);

    void recordUsage(Long userId, ChatClientRequest chatClientRequest, ChatClientResponse chatResponse, long latencyMs);
}
