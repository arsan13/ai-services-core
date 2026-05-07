package com.arsan.ai.service;

import com.arsan.ai.entity.AppUser;

import java.io.IOException;

public interface EmailVerificationService {

    void sendVerificationEmail(AppUser user) throws IOException;

    void resendVerificationEmail(String email) throws IOException;

    void verify(String token);

}
