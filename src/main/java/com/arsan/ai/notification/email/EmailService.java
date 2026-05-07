package com.arsan.ai.notification.email;

import com.arsan.ai.notification.email.model.EmailRequest;

public interface EmailService {

    void send(EmailRequest request);
}
