package com.arsan.ai.service;

import com.arsan.ai.entity.User;

public interface EmailVerificationService {

    void sendVerificationEmail(User user);

    void verify(String token);
}
