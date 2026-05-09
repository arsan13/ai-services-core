package com.arsan.ai.notification.email.listener;

import com.arsan.ai.admin.events.AccessRequestApprovedEvent;
import com.arsan.ai.admin.events.AccessRequestRejectedEvent;
import com.arsan.ai.core.properties.AppProperties;
import com.arsan.ai.notification.email.model.EmailRequest;
import com.arsan.ai.notification.email.service.EmailService;
import com.arsan.ai.notification.email.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_APPROVED_SUBJECT;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_APPROVED_TEMPLATE;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_CREATE_PATH;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_REJECTED_SUBJECT;
import static com.arsan.ai.notification.email.constants.EmailConstants.ACCESS_REQUEST_REJECTED_TEMPLATE;

@Component
@RequiredArgsConstructor
public class EmailAccessRequestEventListener {

    private final AppProperties appProperties;
    private final EmailTemplateService emailTemplateService;
    private final EmailService emailService;

    @EventListener
    public void on(AccessRequestApprovedEvent event) {
        Map<String, Object> model = Map.of(
                "name", event.userName(),
                "requestId", event.requestId(),
                "dashboardLink", appProperties.getFrontendUrl()
        );

        String body = emailTemplateService.render(ACCESS_REQUEST_APPROVED_TEMPLATE, model);

        EmailRequest emailRequest = new EmailRequest(List.of(event.userEmail()), ACCESS_REQUEST_APPROVED_SUBJECT, body);
        emailService.send(emailRequest);
    }

    @EventListener
    public void on(AccessRequestRejectedEvent event) {
        Map<String, Object> model = Map.of(
                "name", event.userName(),
                "requestId", event.requestId(),
                "requestLink", appProperties.getFrontendUrl().concat(ACCESS_REQUEST_CREATE_PATH),
                "reason", event.reason()
        );

        String body = emailTemplateService.render(ACCESS_REQUEST_REJECTED_TEMPLATE, model);

        EmailRequest emailRequest = new EmailRequest(List.of(event.userEmail()), ACCESS_REQUEST_REJECTED_SUBJECT, body);
        emailService.send(emailRequest);
    }
}
