package com.arsan.ai.service.impl;

import com.arsan.ai.entity.AppUser;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.model.auth.ChangePasswordRequest;
import com.arsan.ai.model.auth.ResetPasswordRequest;
import com.arsan.ai.model.common.EmailRequest;
import com.arsan.ai.properties.AppProperties;
import com.arsan.ai.properties.SecurityProperties;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.security.jwt.JwtService;
import com.arsan.ai.service.EmailService;
import com.arsan.ai.service.EmailTemplateService;
import com.arsan.ai.service.PasswordService;
import com.arsan.ai.util.ExceptionUtils;
import com.arsan.ai.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_PATH;
import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_SUBJECT;
import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_TEMPLATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
    private final SecurityProperties securityProperties;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

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
        Optional<AppUser> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            log.warn("Forgot Password requested for non-existent user: {}", email);
            return;
        }
        if (userOpt.get().getPassword() == null) {
            log.warn("Forgot Password requested for OAuth2 user: {}", email);
            return;
        }

        String token = jwtService.generateToken(userOpt.get(), TokenPurpose.PASSWORD_RESET);
        String link = appProperties.getFrontendUrl() + RESET_PASSWORD_PATH + "?token=" + token;

        Map<String, Object> model = Map.of(
                "name", userOpt.get().getFullName(),
                "link", link,
                "expiryMinutes", securityProperties.getJwt().getPasswordResetExpirationInMinutes()
        );
        String body = emailTemplateService.render(RESET_PASSWORD_TEMPLATE, model);

        EmailRequest emailRequest = new EmailRequest(List.of(userOpt.get().getEmail()), RESET_PASSWORD_SUBJECT, body);
        emailService.send(emailRequest);
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
