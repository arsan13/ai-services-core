package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.model.auth.AuthRequest;
import com.arsan.chatbot.model.auth.AuthResponse;
import com.arsan.chatbot.model.auth.AvailabilityResponse;
import com.arsan.chatbot.model.auth.RegisterRequest;
import com.arsan.chatbot.provider.core.OAuthUserInfo;
import com.arsan.chatbot.provider.registry.OAuthUserInfoProviderRegistry;
import com.arsan.chatbot.repository.UserRepository;
import com.arsan.chatbot.resolver.OAuthUserResolver;
import com.arsan.chatbot.security.jwt.JwtService;
import com.arsan.chatbot.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final OAuthUserInfoProviderRegistry oAuthUserInfoProviderRegistry;
    private final OAuthUserResolver oauthUserResolver;

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    public AvailabilityResponse isUsernameAvailable(String username) {
        return new AvailabilityResponse(!userRepository.existsByUsername(username));
    }

    @Override
    @Transactional
    public AuthResponse handleOAuth2LoginRequest(String registrationId, OAuth2User oAuth2User) {
        OAuthUserInfo userInfo = oAuthUserInfoProviderRegistry.get(registrationId, oAuth2User);
        User user = oauthUserResolver.resolve(userInfo);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
