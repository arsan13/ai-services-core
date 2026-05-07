package com.arsan.ai.service.impl;

import com.arsan.ai.entity.AppUser;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.model.common.EmailRequest;
import com.arsan.ai.properties.AppProperties;
import com.arsan.ai.properties.SecurityProperties;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.security.jwt.JwtService;
import com.arsan.ai.service.EmailService;
import com.arsan.ai.service.EmailVerificationService;
import com.arsan.ai.util.EmailTemplateUtil;
import com.arsan.ai.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.arsan.ai.constants.EmailConstants.VERIFY_EMAIL_PATH;
import static com.arsan.ai.constants.EmailConstants.VERIFY_EMAIL_SUBJECT;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final AppProperties appProperties;
    private final SecurityProperties securityProperties;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public void sendVerificationEmail(AppUser user) throws IOException {
        String token = jwtService.generateToken(user, TokenPurpose.EMAIL_VERIFICATION);
        String link = appProperties.getFrontendUrl() + VERIFY_EMAIL_PATH + "?token=" + token;
//        String body = VERIFY_EMAIL_TEMPLATE.formatted(user.getFullName(), link, securityProperties.getJwt().getEmailVerificationExpirationInMinutes());

        String template = EmailTemplateUtil.loadTemplate("verify-email.html");
        Map<String, String> model = Map.of(
                "name", user.getFullName(),
                "link", link,
                "expiryMinutes", String.valueOf(securityProperties.getJwt().getEmailVerificationExpirationInMinutes())
        );
        String body = EmailTemplateUtil.replacePlaceholders(template, model);

        EmailRequest emailRequest = new EmailRequest(List.of(user.getEmail()), VERIFY_EMAIL_SUBJECT, body);
        emailService.send(emailRequest);
    }

    @Override
    public void resendVerificationEmail(String email) throws IOException {
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
