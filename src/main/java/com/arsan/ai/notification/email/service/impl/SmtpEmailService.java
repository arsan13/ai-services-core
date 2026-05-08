package com.arsan.ai.notification.email.service.impl;

import com.arsan.ai.notification.email.service.EmailService;
import com.arsan.ai.notification.email.model.EmailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getTo().toArray(String[]::new));
            helper.setSubject(request.getSubject());
            helper.setText(request.getHtmlBody(), true);

            if (request.getCc() != null && !request.getCc().isEmpty()) {
                helper.setCc(request.getCc().toArray(String[]::new));
            }
            if (request.getBcc() != null && !request.getBcc().isEmpty()) {
                helper.setBcc(request.getBcc().toArray(String[]::new));
            }

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email", e);
        }
    }
}
