package com.arsan.ai.service.impl;

import com.arsan.ai.entity.User;
import com.arsan.ai.mapper.UserMapper;
import com.arsan.ai.model.auth.AuthRequest;
import com.arsan.ai.model.auth.AuthResponse;
import com.arsan.ai.model.auth.AvailabilityResponse;
import com.arsan.ai.model.auth.RegisterRequest;
import com.arsan.ai.provider.oauth2.core.OAuthUserInfo;
import com.arsan.ai.provider.oauth2.registry.OAuthUserInfoProviderRegistry;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.resolver.OAuthUserResolver;
import com.arsan.ai.security.jwt.JwtService;
import com.arsan.ai.service.AuthService;
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
    private final UserMapper userMapper;

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, userMapper.toUserProfile(user));
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, userMapper.toUserProfile(user));
    }

    @Override
    public AvailabilityResponse isEmailAvailable(String email) {
        return new AvailabilityResponse(!userRepository.existsByEmail(email));
    }

    @Override
    @Transactional
    public String handleOAuth2LoginRequest(String registrationId, OAuth2User oAuth2User) {
        OAuthUserInfo oAuthUserInfo = oAuthUserInfoProviderRegistry.get(registrationId, oAuth2User);
        User user = oauthUserResolver.resolve(oAuthUserInfo);
        return jwtService.generateToken(user);
    }
}
