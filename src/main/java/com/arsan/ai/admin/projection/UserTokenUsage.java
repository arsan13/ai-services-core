package com.arsan.ai.admin.projection;

public interface UserTokenUsage {
    String getUserId();

    Long getTotalTokens();

    Double getCostInUsd();
}
