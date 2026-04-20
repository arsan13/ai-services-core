package com.arsan.chatbot.provider.github;

import com.arsan.chatbot.enums.AuthProviderType;
import com.arsan.chatbot.provider.core.OAuthUserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GithubUserInfo implements OAuthUserInfo {

    private final OAuth2User user;

    public GithubUserInfo(OAuth2User user) {
        this.user = user;
    }

    public String getEmail() {
        return user.getAttribute("email");
    }

    public String getName() {
        return user.getAttribute("name");
    }

    public String getProviderId() {
        return user.getAttribute("id");
    }

    public AuthProviderType getProviderType() {
        return AuthProviderType.GITHUB;
    }
}
