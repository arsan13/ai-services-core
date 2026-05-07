package com.arsan.ai.service.auth.impl;

import com.arsan.ai.entity.AppUser;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.events.PasswordResetRequestedEvent;
import com.arsan.ai.model.auth.ChangePasswordRequest;
import com.arsan.ai.model.auth.ResetPasswordRequest;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.security.jwt.JwtService;
import com.arsan.ai.service.auth.PasswordService;
import com.arsan.ai.util.ExceptionUtils;
import com.arsan.ai.util.SecurityUtils;
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
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void changePassword(ChangePasswordRequest request) {
        AppUser user = SecurityUtils.getCurrentUser().orElseThrow(ExceptionUtils::userNotFound);

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
        userRepository.findByEmail(email).map(PasswordResetRequestedEvent::new).ifPresent(eventPublisher::publishEvent);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        jwtService.validateToken(request.getToken(), TokenPurpose.PASSWORD_RESET);

        String email = jwtService.extractEmail(request.getToken());
        AppUser user = userRepository.findByEmail(email).orElseThrow(ExceptionUtils::userNotFound);

        validateTokenReuse(request, user);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetDate(LocalDateTime.now());
        userRepository.save(user);
    }

    private void validateTokenReuse(ResetPasswordRequest request, AppUser user) {
        Instant issuedAt = jwtService.extractIssuedAt(request.getToken()).toInstant();
        Instant resetAt = Optional.ofNullable(user.getPasswordResetDate()).map(dt -> dt.atZone(ZoneId.systemDefault()).toInstant()).orElse(null);
        if (resetAt != null && issuedAt.isBefore(resetAt)) {
            throw new IllegalArgumentException("Token already used");
        }
    }
}
