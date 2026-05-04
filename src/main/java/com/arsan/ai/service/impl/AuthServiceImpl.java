package com.arsan.ai.service.impl;

import com.arsan.ai.entity.User;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.mapper.UserMapper;
import com.arsan.ai.model.auth.AuthRequest;
import com.arsan.ai.model.auth.AuthResponse;
import com.arsan.ai.model.auth.AvailabilityResponse;
import com.arsan.ai.model.auth.ChangePasswordRequest;
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
import com.arsan.ai.util.ExceptionUtils;
import com.arsan.ai.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_PATH;
import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_SUBJECT;
import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_TEMPLATE;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public void changePassword(ChangePasswordRequest request) {
        User user = SecurityUtils.getCurrentUser().orElseThrow(ExceptionUtils::userNotFound);

        if (user.getPassword() == null) {
            throw new IllegalStateException("Password change not allowed for this account");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different");
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetDate(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public void forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            log.warn("Forgot Password requested for non-existent user: {}", email);
            return;
        }
        if (userOpt.get().getPassword() == null) {
            log.warn("Forgot Password requested for OAuth2 user: {}", email);
            return;
        }

        String token = jwtService.generateToken(userOpt.get(), TokenPurpose.PASSWORD_RESET);
        String emailLink = appProperties.getFrontendUrl() + RESET_PASSWORD_PATH + "?token=" + token;
        String body = RESET_PASSWORD_TEMPLATE.formatted(userOpt.get().getFullName(), emailLink, securityProperties.getJwt().getPasswordResetExpirationInMinutes());

        emailService.send(userOpt.get().getEmail(), RESET_PASSWORD_SUBJECT, body);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        jwtService.validateToken(request.getToken(), TokenPurpose.PASSWORD_RESET);

        String email = jwtService.extractEmail(request.getToken());
        User user = userRepository.findByEmail(email).orElseThrow(ExceptionUtils::userNotFound);

        validateTokenReuse(request, user);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetDate(LocalDateTime.now());
        userRepository.save(user);
    }

    private void validateTokenReuse(ResetPasswordRequest request, User user) {
        Instant issuedAt = jwtService.extractIssuedAt(request.getToken()).toInstant();
        Instant resetAt = Optional.ofNullable(user.getPasswordResetDate()).map(dt -> dt.atZone(ZoneId.systemDefault()).toInstant()).orElse(null);
        if (resetAt != null && issuedAt.isBefore(resetAt)) {
            throw new IllegalArgumentException("Token already used");
        }
    }
}
