package com.arsan.ai.auth.provider.core;

import com.arsan.ai.auth.enums.AuthProviderType;

public interface OAuthUserInfo {
    String getEmail();

    String getName();

    String getProviderId();

    AuthProviderType getProviderType();
}
