package com.arsan.ai.provider.google;

import com.arsan.ai.enums.AuthProviderType;
import com.arsan.ai.provider.core.OAuthUserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleUserInfo implements OAuthUserInfo {

    private final OAuth2User user;

    public GoogleUserInfo(OAuth2User user) {
        this.user = user;
    }

    public String getEmail() {
        return user.getAttribute("email");
    }

    public String getName() {
        return user.getAttribute("name");
    }

    public String getProviderId() {
        return user.getAttribute("sub");
    }

    public AuthProviderType getProviderType() {
        return AuthProviderType.GOOGLE;
    }
}
