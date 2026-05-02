package com.arsan.ai.service.impl;

import com.arsan.ai.entity.User;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.exception.custom.ResourceNotFoundException;
import com.arsan.ai.properties.AppProperties;
import com.arsan.ai.properties.EmailVerificationProperties;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.security.jwt.JwtService;
import com.arsan.ai.service.EmailService;
import com.arsan.ai.service.EmailVerificationService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.arsan.ai.constants.EmailConstants.VERIFY_PATH;
import static com.arsan.ai.constants.EmailConstants.VERIFY_SUBJECT;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final AppProperties appProperties;
    private final EmailVerificationProperties emailVerificationProperties;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public void sendVerificationEmail(User user) {
        String token = jwtService.generateVerificationToken(user);
        String link = appProperties.getFrontendUrl() + VERIFY_PATH + "?token=" + token;

        final String template = """
                Hi %s,
                
                Please click the link below to verify your email address:
                
                %s
                
                This link will expire in %d minutes.
                
                If you did not create an account, please ignore this email.
                
                Best regards,
                The AI Services Core Team
                """;

        String body = template.formatted(user.getFullName(), link, emailVerificationProperties.getExpirationInMinutes());

        emailService.send(user.getEmail(), VERIFY_SUBJECT, body);
    }

    @Override
    public void verify(String token) {
        if (!jwtService.hasPurpose(token, TokenPurpose.EMAIL_VERIFICATION)) {
            throw new JwtException("Invalid token");
        }

        if (jwtService.isVerificationTokenExpired(token)) {
            throw new JwtException("Token expired");
        }

        String email = jwtService.extractEmailFromVerificationToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        user.setVerified(true);
        user.setVerifiedDate(LocalDateTime.now());
        userRepository.save(user);
    }
}
