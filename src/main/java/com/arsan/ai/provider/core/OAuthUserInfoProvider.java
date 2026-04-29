package com.arsan.ai.provider.core;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuthUserInfoProvider {
    boolean supports(String registrationId);

    OAuthUserInfo create(OAuth2User user);
}
