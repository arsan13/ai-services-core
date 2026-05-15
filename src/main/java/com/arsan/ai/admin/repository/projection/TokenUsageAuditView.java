package com.arsan.ai.admin.repository.projection;

import java.time.LocalDateTime;

public interface TokenUsageAuditView {

    Long getId();

    String getModel();

    String getProvider();

    int getPromptTokens();

    int getCompletionTokens();

    int getTotalTokens();

    double getCostInUsd();

    double getLatencySec();

    String getInputSummary();

    String getOutputSummary();

    LocalDateTime getCreatedDate();

    Long getUserId();
}
