package com.arsan.ai.service.notification.email;

import com.arsan.ai.model.common.EmailRequest;

public interface EmailService {

    void send(EmailRequest request);
}
