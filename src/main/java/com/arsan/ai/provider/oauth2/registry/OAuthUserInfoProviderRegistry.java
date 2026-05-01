package com.arsan.ai.provider.oauth2.registry;

import com.arsan.ai.provider.oauth2.core.OAuthUserInfo;
import com.arsan.ai.provider.oauth2.core.OAuthUserInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthUserInfoProviderRegistry {

    private final List<OAuthUserInfoProvider> providers;

    public OAuthUserInfo get(String registrationId, OAuth2User user) {

        return providers.stream()
                .filter(p -> p.supports(registrationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported provider: " + registrationId))
                .create(user);
    }
}
