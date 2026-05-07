package com.arsan.ai.service.impl;

import com.arsan.ai.model.common.EmailRequest;
import com.arsan.ai.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.email.provider", havingValue = "smtp")
@Slf4j
@RequiredArgsConstructor
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Async("emailTaskExecutor")
    @Override
    public void send(EmailRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getTo().toArray(String[]::new));
            message.setSubject(request.getSubject());
            message.setText(request.getBody());

            if (request.getCc() != null && !request.getCc().isEmpty()) {
                message.setCc(request.getCc().toArray(String[]::new));
            }
            if (request.getBcc() != null && !request.getBcc().isEmpty()) {
                message.setBcc(request.getBcc().toArray(String[]::new));
            }

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email", e);
        }
    }
}
