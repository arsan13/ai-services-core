package com.arsan.ai.security.handler;

import com.arsan.ai.security.OAuth2RedirectService;
import com.arsan.ai.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final OAuth2RedirectService redirectService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            String jwt = authService.handleOAuth2LoginRequest(token.getAuthorizedClientRegistrationId(), oAuth2User);

            String redirectUrl = redirectService.buildSuccessUrl(jwt);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            String redirectUrl = redirectService.buildErrorUrl(errorMessage);
            response.sendRedirect(redirectUrl);
        }
    }
}
