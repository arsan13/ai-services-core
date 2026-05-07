package com.arsan.ai.service.auth.impl;

import com.arsan.ai.entity.AppUser;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.security.jwt.JwtService;
import com.arsan.ai.service.auth.EmailVerificationService;
import com.arsan.ai.service.notification.email.EmailNotificationService;
import com.arsan.ai.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailNotificationService emailNotificationService;

    @Override
    public void sendVerificationEmail(AppUser user) {
        emailNotificationService.sendEmailVerificationEmail(user);
    }

    @Override
    public void resendVerificationEmail(String email) {
        AppUser user = userRepository.findByEmail(email).orElseThrow(ExceptionUtils::userNotFound);

        if (user.isVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        sendVerificationEmail(user);
    }

    @Override
    public void verify(String token) {
        jwtService.validateToken(token, TokenPurpose.EMAIL_VERIFICATION);

        String email = jwtService.extractEmail(token);
        AppUser user = userRepository.findByEmail(email).orElseThrow(ExceptionUtils::userNotFound);

        user.markAsVerified();
        userRepository.save(user);
    }
}
