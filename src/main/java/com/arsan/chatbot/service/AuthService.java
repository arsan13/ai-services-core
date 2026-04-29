package com.arsan.chatbot.service;

import com.arsan.chatbot.model.auth.AuthRequest;
import com.arsan.chatbot.model.auth.AuthResponse;
import com.arsan.chatbot.model.auth.AvailabilityResponse;
import com.arsan.chatbot.model.auth.RegisterRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    AuthResponse register(RegisterRequest authRequest);

    AvailabilityResponse isUsernameAvailable(String username);

    String handleOAuth2LoginRequest(String registrationId, OAuth2User oAuth2User);
}
