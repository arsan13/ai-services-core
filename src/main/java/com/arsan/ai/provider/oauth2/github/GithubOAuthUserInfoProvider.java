package com.arsan.ai.provider.oauth2.github;

import com.arsan.ai.provider.oauth2.core.OAuthUserInfo;
import com.arsan.ai.provider.oauth2.core.OAuthUserInfoProvider;
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
