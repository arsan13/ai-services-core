package com.arsan.ai.notification.email.listener;

import com.arsan.ai.admin.events.AccessRequestApprovedEvent;
import com.arsan.ai.admin.events.AccessRequestRejectedEvent;
import com.arsan.ai.admin.events.AccessRequestRevokedEvent;
import com.arsan.ai.core.properties.AppProperties;
import com.arsan.ai.notification.email.model.EmailRequest;
import com.arsan.ai.notification.email.service.EmailService;
import com.arsan.ai.notification.email.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;

import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_APPROVED_SUBJECT;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_APPROVED_TEMPLATE;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_CREATE_PATH;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_REJECTED_SUBJECT;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_REJECTED_TEMPLATE;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_REVOKED_SUBJECT;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_REVOKED_TEMPLATE;

@Component
@RequiredArgsConstructor
public class EmailAccessRequestEventListener {

    private static final String NAME = "name";
    private static final String REQUEST_ID = "requestId";
    private static final String REQUEST_LINK = "requestLink";
    private static final String REASON = "reason";

    private final AppProperties appProperties;
    private final EmailTemplateService emailTemplateService;
    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(AccessRequestApprovedEvent event) {
        Map<String, Object> model = Map.of(
                NAME, event.userName(),
                REQUEST_ID, event.requestId(),
                REQUEST_LINK, appProperties.getFrontendUrl()
        );

        sendEmail(event.userEmail(), ACCESS_REQUEST_APPROVED_SUBJECT, ACCESS_REQUEST_APPROVED_TEMPLATE, model);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(AccessRequestRejectedEvent event) {
        Map<String, Object> model = Map.of(
                NAME, event.userName(),
                REQUEST_ID, event.requestId(),
                REQUEST_LINK, appProperties.getFrontendUrl().concat(ACCESS_REQUEST_CREATE_PATH),
                REASON, event.reason()
        );

        sendEmail(event.userEmail(), ACCESS_REQUEST_REJECTED_SUBJECT, ACCESS_REQUEST_REJECTED_TEMPLATE, model);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(AccessRequestRevokedEvent event) {
        Map<String, Object> model = Map.of(
                NAME, event.userName(),
                REQUEST_ID, event.requestId(),
                REASON, event.reason(),
                REQUEST_LINK, appProperties.getFrontendUrl().concat(ACCESS_REQUEST_CREATE_PATH)
        );

        sendEmail(event.userEmail(), ACCESS_REQUEST_REVOKED_SUBJECT, ACCESS_REQUEST_REVOKED_TEMPLATE, model);
    }

    private void sendEmail(String recipient, String subject, String template, Map<String, Object> model) {
        String body = emailTemplateService.render(template, model);
        EmailRequest request = new EmailRequest(List.of(recipient), subject, body);

        emailService.send(request);
    }
}
