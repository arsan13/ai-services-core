package com.arsan.ai.security;

import com.arsan.ai.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2RedirectService {

    private final AppProperties appProperties;

    public String buildSuccessUrl(String token) {
        return UriComponentsBuilder
                .fromUriString(appProperties.getFrontendUrl())
                .path("/oauth-success")
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    public String buildErrorUrl(String errorMsg) {
        return UriComponentsBuilder
                .fromUriString(appProperties.getFrontendUrl())
                .path("/oauth-error")
                .queryParam("message", errorMsg)
                .build()
                .toUriString();
    }
}
