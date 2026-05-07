package com.arsan.ai.listener;

import com.arsan.ai.entity.AppUser;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.events.EmailVerificationRequestedEvent;
import com.arsan.ai.events.PasswordResetRequestedEvent;
import com.arsan.ai.model.common.EmailRequest;
import com.arsan.ai.properties.AppProperties;
import com.arsan.ai.properties.SecurityProperties;
import com.arsan.ai.security.jwt.JwtService;
import com.arsan.ai.service.notification.email.EmailService;
import com.arsan.ai.service.notification.email.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_PATH;
import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_SUBJECT;
import static com.arsan.ai.constants.EmailConstants.RESET_PASSWORD_TEMPLATE;
import static com.arsan.ai.constants.EmailConstants.VERIFY_EMAIL_PATH;
import static com.arsan.ai.constants.EmailConstants.VERIFY_EMAIL_SUBJECT;
import static com.arsan.ai.constants.EmailConstants.VERIFY_EMAIL_TEMPLATE;

@Component
@RequiredArgsConstructor
public class EmailNotificationEventListener {

    private final AppProperties appProperties;
    private final SecurityProperties securityProperties;
    private final JwtService jwtService;
    private final EmailTemplateService emailTemplateService;
    private final EmailService emailService;

    @EventListener
    public void on(EmailVerificationRequestedEvent event) {
        AppUser user = event.user();

        String token = jwtService.generateToken(user, TokenPurpose.EMAIL_VERIFICATION);
        String link = appProperties.getFrontendUrl() + VERIFY_EMAIL_PATH + "?token=" + token;

        Map<String, Object> model = Map.of(
                "name", user.getFullName(),
                "link", link,
                "expiryMinutes", securityProperties.getJwt().getEmailVerificationExpirationInMinutes()
        );
        String body = emailTemplateService.render(VERIFY_EMAIL_TEMPLATE, model);

        EmailRequest emailRequest = new EmailRequest(List.of(user.getEmail()), VERIFY_EMAIL_SUBJECT, body);
        emailService.send(emailRequest);
    }

    @EventListener
    public void on(PasswordResetRequestedEvent event) {
        AppUser user = event.user();

        String token = jwtService.generateToken(user, TokenPurpose.PASSWORD_RESET);
        String link = appProperties.getFrontendUrl() + RESET_PASSWORD_PATH + "?token=" + token;

        Map<String, Object> model = Map.of(
                "name", user.getFullName(),
                "link", link,
                "expiryMinutes", securityProperties.getJwt().getPasswordResetExpirationInMinutes()
        );
        String body = emailTemplateService.render(RESET_PASSWORD_TEMPLATE, model);

        EmailRequest emailRequest = new EmailRequest(List.of(user.getEmail()), RESET_PASSWORD_SUBJECT, body);
        emailService.send(emailRequest);
    }
}
