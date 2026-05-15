package com.arsan.ai.notification.email.listener;

import com.arsan.ai.auth.enums.TokenPurpose;
import com.arsan.ai.auth.events.EmailVerificationRequestedEvent;
import com.arsan.ai.auth.events.PasswordResetRequestedEvent;
import com.arsan.ai.core.properties.AppProperties;
import com.arsan.ai.core.properties.SecurityProperties;
import com.arsan.ai.core.security.service.JwtService;
import com.arsan.ai.notification.email.model.EmailRequest;
import com.arsan.ai.notification.email.service.EmailService;
import com.arsan.ai.notification.email.service.EmailTemplateService;
import com.arsan.ai.shared.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;

import static com.arsan.ai.notification.email.constants.EmailConstants.RESET_PASSWORD_PATH;
import static com.arsan.ai.notification.email.constants.EmailConstants.RESET_PASSWORD_SUBJECT;
import static com.arsan.ai.notification.email.constants.EmailConstants.RESET_PASSWORD_TEMPLATE;
import static com.arsan.ai.notification.email.constants.EmailConstants.VERIFY_EMAIL_PATH;
import static com.arsan.ai.notification.email.constants.EmailConstants.VERIFY_EMAIL_SUBJECT;
import static com.arsan.ai.notification.email.constants.EmailConstants.VERIFY_EMAIL_TEMPLATE;

@Component
@RequiredArgsConstructor
public class EmailAuthEventListener {

    private final AppProperties appProperties;
    private final SecurityProperties securityProperties;
    private final JwtService jwtService;
    private final EmailTemplateService emailTemplateService;
    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(EmailVerificationRequestedEvent event) {
        sendAuthEmail(
                event.user(),
                TokenPurpose.EMAIL_VERIFICATION,
                VERIFY_EMAIL_PATH,
                VERIFY_EMAIL_TEMPLATE,
                VERIFY_EMAIL_SUBJECT,
                securityProperties.getJwt().getEmailVerificationExpirationInMinutes()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(PasswordResetRequestedEvent event) {
        sendAuthEmail(
                event.user(),
                TokenPurpose.PASSWORD_RESET,
                RESET_PASSWORD_PATH,
                RESET_PASSWORD_TEMPLATE,
                RESET_PASSWORD_SUBJECT,
                securityProperties.getJwt().getPasswordResetExpirationInMinutes()
        );
    }

    private void sendAuthEmail(
            AppUser user,
            TokenPurpose purpose,
            String path,
            String template,
            String subject,
            long expiryMinutes
    ) {
        String token = jwtService.generateToken(user, purpose);
        String link = appProperties.getFrontendUrl() + path + "?token=" + token;

        Map<String, Object> model = Map.of(
                "name", user.getFullName(),
                "link", link,
                "expiryMinutes", expiryMinutes
        );

        String body = emailTemplateService.render(template, model);

        emailService.send(new EmailRequest(List.of(user.getEmail()), subject, body));
    }
}
