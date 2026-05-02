package com.arsan.ai.service.impl;

import com.arsan.ai.entity.User;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.exception.custom.ResourceNotFoundException;
import com.arsan.ai.properties.AppProperties;
import com.arsan.ai.properties.SecurityProperties;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.security.jwt.JwtService;
import com.arsan.ai.service.EmailService;
import com.arsan.ai.service.EmailVerificationService;
import com.arsan.ai.util.SecurityUtils;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.arsan.ai.constants.EmailConstants.EMAIL_VERIFY_TEMPLATE;
import static com.arsan.ai.constants.EmailConstants.VERIFY_PATH;
import static com.arsan.ai.constants.EmailConstants.VERIFY_SUBJECT;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final AppProperties appProperties;
    private final SecurityProperties securityProperties;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public void sendVerificationEmail(User user) {
        String token = jwtService.generateToken(user, TokenPurpose.EMAIL_VERIFICATION);
        String link = appProperties.getFrontendUrl() + VERIFY_PATH + "?token=" + token;

        String body = EMAIL_VERIFY_TEMPLATE.formatted(user.getFullName(), link, securityProperties.getJwt().getEmailVerificationExpirationInMinutes());

        emailService.send(user.getEmail(), VERIFY_SUBJECT, body);
    }

    @Override
    public void resendVerificationEmail() {
        User user = SecurityUtils.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        sendVerificationEmail(user);
    }

    @Override
    public void verify(String token) {
        if (!jwtService.hasPurpose(token, TokenPurpose.EMAIL_VERIFICATION)) {
            throw new JwtException("Invalid token");
        }

        if (jwtService.isTokenExpired(token)) {
            throw new JwtException("Token expired");
        }

        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        user.setVerified(true);
        user.setVerifiedDate(LocalDateTime.now());
        userRepository.save(user);
    }
}
