package com.arsan.chatbot.service;

import com.arsan.chatbot.entity.TokenUsageAudit;
import com.arsan.chatbot.proection.UserTokenUsage;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenUsageAuditService {

    List<TokenUsageAudit> findAll();

    List<TokenUsageAudit> findByUserId(String userId);

    List<TokenUsageAudit> getTodayAudits();

    List<TokenUsageAudit> getAuditsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Long getTodayTotalTokens();

    Long getTotalTokensByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Long getTodayUsageByUser(Long userId);

    List<UserTokenUsage> getTodayUsageOfAllUsers();

    TokenUsageAudit auditTokenUsage(ChatClientRequest chatClientRequest, ChatClientResponse chatResponse, long latencyMs);
}
