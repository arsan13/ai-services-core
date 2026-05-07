package com.arsan.ai.service.notification.email.impl;

import com.arsan.ai.model.common.EmailRequest;
import com.arsan.ai.properties.EmailProperties;
import com.arsan.ai.service.notification.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.arsan.ai.constants.BrevoEmailKeyConstants.API_KEY_HEADER;
import static com.arsan.ai.constants.BrevoEmailKeyConstants.BCC;
import static com.arsan.ai.constants.BrevoEmailKeyConstants.CC;
import static com.arsan.ai.constants.BrevoEmailKeyConstants.EMAIL;
import static com.arsan.ai.constants.BrevoEmailKeyConstants.HTML_CONTENT;
import static com.arsan.ai.constants.BrevoEmailKeyConstants.NAME;
import static com.arsan.ai.constants.BrevoEmailKeyConstants.SENDER;
import static com.arsan.ai.constants.BrevoEmailKeyConstants.SUBJECT;
import static com.arsan.ai.constants.BrevoEmailKeyConstants.TO;

@Service
@ConditionalOnProperty(name = "app.email.provider", havingValue = "brevo")
@Slf4j
@RequiredArgsConstructor
public class BrevoEmailService implements EmailService {

    private final WebClient webClient;
    private final EmailProperties properties;

    @Async("emailTaskExecutor")
    @Override
    public void send(EmailRequest request) {
        Map<String, Object> payload = new HashMap<>();

        payload.put(SENDER, Map.of(
                NAME, properties.getBrevo().getSenderName(),
                EMAIL, properties.getBrevo().getSenderEmail()
        ));

        payload.put(TO, mapEmails(request.getTo()));

        if (request.getCc() != null && !request.getCc().isEmpty()) {
            payload.put(CC, mapEmails(request.getCc()));
        }

        if (request.getBcc() != null && !request.getBcc().isEmpty()) {
            payload.put(BCC, mapEmails(request.getBcc()));
        }

        payload.put(SUBJECT, request.getSubject());
        payload.put(HTML_CONTENT, request.getHtmlBody());

        webClient.post()
                .uri(properties.getBrevo().getUrl())
                .header(API_KEY_HEADER, properties.getBrevo().getApiKey())
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(res -> log.info("Email sent to {}", request.getTo()))
                .doOnError(err -> log.error("Email failed to {}", request.getTo(), err))
                .subscribe();
    }

    private List<Map<String, String>> mapEmails(List<String> emails) {
        return emails.stream()
                .map(email -> Map.of(EMAIL, email))
                .toList();
    }
}
