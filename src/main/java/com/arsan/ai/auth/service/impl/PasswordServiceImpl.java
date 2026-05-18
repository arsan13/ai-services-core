package com.arsan.ai.auth.service.impl;

import com.arsan.ai.auth.enums.TokenPurpose;
import com.arsan.ai.auth.events.PasswordResetRequestedEvent;
import com.arsan.ai.auth.model.ResetPasswordRequest;
import com.arsan.ai.auth.service.PasswordService;
import com.arsan.ai.core.security.service.JwtService;
import com.arsan.ai.shared.cache.AppUserCache;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AppUserCache userCache;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void forgotPassword(String email) {
        AppUser user = userCache.getByEmail(email);

        if (user.getPassword() == null) {
            throw new IllegalStateException("Password reset is not available for OAuth2 accounts. Please sign in using " + user.getProviderType());
        }

        eventPublisher.publishEvent(new PasswordResetRequestedEvent(user));
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        jwtService.validateToken(request.getToken(), TokenPurpose.PASSWORD_RESET);

        String email = jwtService.extractEmail(request.getToken());
        AppUser user = userCache.getByEmail(email);

        validateTokenReuse(request, user);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetDate(LocalDateTime.now());
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);
        userCache.evict(user);
    }

    private void validateTokenReuse(ResetPasswordRequest request, AppUser user) {
        Instant issuedAt = jwtService.extractIssuedAt(request.getToken()).toInstant();
        Instant resetAt = Optional.ofNullable(user.getPasswordResetDate()).map(dt -> dt.atZone(ZoneId.systemDefault()).toInstant()).orElse(null);
        if (resetAt != null && issuedAt.isBefore(resetAt)) {
            throw new IllegalArgumentException("Token already used");
        }
    }
}
