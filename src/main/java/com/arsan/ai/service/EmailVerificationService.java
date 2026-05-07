package com.arsan.ai.service;

import com.arsan.ai.entity.AppUser;

public interface EmailVerificationService {

    void sendVerificationEmail(AppUser user);

    void resendVerificationEmail(String email);

    void verify(String token);

}
