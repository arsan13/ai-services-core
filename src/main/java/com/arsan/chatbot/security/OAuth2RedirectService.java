package com.arsan.chatbot.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2RedirectService {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public String buildSuccessUrl(String token) {
        return UriComponentsBuilder
                .fromUriString(frontendUrl)
                .path("/oauth-success")
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    public String buildErrorUrl(String errorMsg) {
        return UriComponentsBuilder
                .fromUriString(frontendUrl)
                .path("/oauth-error")
                .queryParam("message", errorMsg)
                .build()
                .toUriString();
    }
}
