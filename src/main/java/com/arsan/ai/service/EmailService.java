package com.arsan.ai.service;

import com.arsan.ai.model.common.EmailRequest;

public interface EmailService {

    void send(EmailRequest request);
}
