package com.arsan.ai.provider.core;

import com.arsan.ai.enums.AuthProviderType;

public interface OAuthUserInfo {
    String getEmail();

    String getName();

    String getProviderId();

    AuthProviderType getProviderType();
}
