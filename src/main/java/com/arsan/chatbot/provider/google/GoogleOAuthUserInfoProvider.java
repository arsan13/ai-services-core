package com.arsan.chatbot.provider.google;

import com.arsan.chatbot.provider.core.OAuthUserInfo;
import com.arsan.chatbot.provider.core.OAuthUserInfoProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthUserInfoProvider implements OAuthUserInfoProvider {

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equalsIgnoreCase("google");
    }

    @Override
    public OAuthUserInfo create(OAuth2User user) {
        return new GoogleUserInfo(user);
    }
}
