package com.arsan.chatbot.provider.core;

import com.arsan.chatbot.enums.AuthProviderType;

public interface OAuthUserInfo {
    String getEmail();

    String getName();

    String getProviderId();

    AuthProviderType getProviderType();
}
