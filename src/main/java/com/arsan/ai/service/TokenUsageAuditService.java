package com.arsan.ai.service;

import com.arsan.ai.entity.User;
import com.arsan.ai.projection.TokenUsageAuditView;
import com.arsan.ai.projection.UserTokenUsage;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenUsageAuditService {

    List<TokenUsageAuditView> getAll(int page, int size);

    List<TokenUsageAuditView> getByUserId(Long userId);

    List<TokenUsageAuditView> getAuditsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Long getTotalTokens(LocalDateTime startDate, LocalDateTime endDate);

    Long getTotalTokensByUser(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<UserTokenUsage> getUserTokenUsageSummary(LocalDateTime startDate, LocalDateTime endDate);

    void recordUsage(User user, ChatClientRequest chatClientRequest, ChatClientResponse chatResponse, long latencyMs);
}
