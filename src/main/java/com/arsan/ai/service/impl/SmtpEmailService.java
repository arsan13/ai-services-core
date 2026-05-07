package com.arsan.ai.service.impl;

import com.arsan.ai.model.common.EmailRequest;
import com.arsan.ai.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Async("emailTaskExecutor")
    @Override
    public void send(EmailRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getBody());

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to {}", request.getTo(), e);
        }
    }
}
