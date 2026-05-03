package com.arsan.ai.service.impl;

import com.arsan.ai.entity.User;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.exception.custom.ResourceNotFoundException;
import com.arsan.ai.mapper.UserMapper;
import com.arsan.ai.model.auth.AuthRequest;
import com.arsan.ai.model.auth.AuthResponse;
import com.arsan.ai.model.auth.AvailabilityResponse;
import com.arsan.ai.model.auth.RegisterRequest;
import com.arsan.ai.model.auth.ResetPasswordRequest;
import com.arsan.ai.properties.AppProperties;
import com.arsan.ai.properties.SecurityProperties;
import com.arsan.ai.provider.oauth2.core.OAuthUserInfo;
import com.arsan.ai.provider.oauth2.registry.OAuthUserInfoProviderRegistry;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.resolver.OAuthUserResolver;
import com.arsan.ai.security.jwt.JwtService;
import com.arsan.ai.service.AuthService;
import com.arsan.ai.service.EmailService;
import com.arsan.ai.service.EmailVerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_PATH;
import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_SUBJECT;
import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_TEMPLATE;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
    private final SecurityProperties securityProperties;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    private final OAuthUserInfoProviderRegistry oAuthUserInfoProviderRegistry;
    private final OAuthUserResolver oauthUserResolver;
    private final UserMapper userMapper;

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user, TokenPurpose.ACCESS);
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
        emailVerificationService.sendVerificationEmail(user);
        String token = jwtService.generateToken(user, TokenPurpose.ACCESS);
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
        return jwtService.generateToken(user, TokenPurpose.ACCESS);
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String token = jwtService.generateToken(user, TokenPurpose.PASSWORD_RESET);

        String emailLink = appProperties.getFrontendUrl() + RESET_PASSWORD_PATH + "?token=" + token;
        String body = RESET_PASSWORD_TEMPLATE.formatted(user.getFullName(), emailLink, securityProperties.getJwt().getPasswordResetExpirationInMinutes());

        emailService.send(user.getEmail(), RESET_PASSWORD_SUBJECT, body);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (jwtService.hasInvalidPurpose(request.getToken(), TokenPurpose.PASSWORD_RESET)) {
            throw new IllegalArgumentException("Invalid token purpose");
        }

        if (jwtService.isTokenExpired(request.getToken())) {
            throw new IllegalArgumentException("Token expired");
        }

        String email = jwtService.extractEmail(request.getToken());
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Instant issuedAt = jwtService.extractIssuedAt(request.getToken()).toInstant();
        Instant resetAt = user.getPasswordResetDate().atZone(ZoneId.systemDefault()).toInstant();
        if (resetAt != null && issuedAt.isBefore(resetAt)) {
            throw new IllegalArgumentException("Token already used");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetDate(LocalDateTime.now());
        userRepository.save(user);
    }
}
