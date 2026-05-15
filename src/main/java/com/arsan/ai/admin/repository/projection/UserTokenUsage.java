package com.arsan.ai.admin.repository.projection;

public interface UserTokenUsage {
    String getUserId();

    Long getTotalTokens();

    Double getCostInUsd();
}
