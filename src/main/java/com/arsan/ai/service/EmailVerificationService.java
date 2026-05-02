package com.arsan.ai.service;

import com.arsan.ai.entity.User;

public interface EmailVerificationService {

    void sendVerificationEmail(User user);

    void resendVerificationEmail();

    void verify(String token);

}
