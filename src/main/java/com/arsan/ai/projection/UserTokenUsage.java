package com.arsan.ai.projection;

public interface UserTokenUsage {
    String getUserId();

    Long getTotalTokens();

    Double getCostInUsd();
}
