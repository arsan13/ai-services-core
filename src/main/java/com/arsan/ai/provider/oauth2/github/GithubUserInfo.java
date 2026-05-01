package com.arsan.ai.provider.oauth2.github;

import com.arsan.ai.enums.AuthProviderType;
import com.arsan.ai.provider.oauth2.core.OAuthUserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Objects;

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
        return Objects.toString(user.getAttribute("id"), user.getAttribute("login"));
    }

    public AuthProviderType getProviderType() {
        return AuthProviderType.GITHUB;
    }
}
