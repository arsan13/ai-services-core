package com.arsan.chatbot.provider.github;

import com.arsan.chatbot.provider.core.OAuthUserInfo;
import com.arsan.chatbot.provider.core.OAuthUserInfoProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GithubOAuthUserInfoProvider implements OAuthUserInfoProvider {

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equalsIgnoreCase("github");
    }

    @Override
    public OAuthUserInfo create(OAuth2User user) {
        return new GithubUserInfo(user);
    }
}
