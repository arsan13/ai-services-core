package com.arsan.ai.service.auth;

import com.arsan.ai.entity.AppUser;

public interface EmailVerificationService {

    void sendVerificationEmail(AppUser user);

    void resendVerificationEmail(String email);

    void verify(String token);

}
