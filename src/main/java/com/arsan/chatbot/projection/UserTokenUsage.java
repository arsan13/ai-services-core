package com.arsan.chatbot.projection;

public interface UserTokenUsage {
    String getUserId();

    Long getTotalTokens();

    Double getCostInUsd();
}
