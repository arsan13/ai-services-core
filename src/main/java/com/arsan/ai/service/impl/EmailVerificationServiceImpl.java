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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.arsan.ai.constants.EmailConstants.VERIFY_EMAIL_PATH;
import static com.arsan.ai.constants.EmailConstants.VERIFY_EMAIL_SUBJECT;
import static com.arsan.ai.constants.EmailConstants.VERIFY_EMAIL_TEMPLATE;

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
        String link = appProperties.getFrontendUrl() + VERIFY_EMAIL_PATH + "?token=" + token;
        String body = VERIFY_EMAIL_TEMPLATE.formatted(user.getFullName(), link, securityProperties.getJwt().getEmailVerificationExpirationInMinutes());

        emailService.send(user.getEmail(), VERIFY_EMAIL_SUBJECT, body);
    }

    @Override
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        sendVerificationEmail(user);
    }

    @Override
    public void verify(String token) {
        if (jwtService.hasInvalidPurpose(token, TokenPurpose.EMAIL_VERIFICATION)) {
            throw new IllegalArgumentException("Invalid token purpose");
        }

        if (jwtService.isTokenExpired(token)) {
            throw new IllegalArgumentException("Token expired");
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
