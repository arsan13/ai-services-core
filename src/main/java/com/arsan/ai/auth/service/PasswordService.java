package com.arsan.ai.auth.service;

import com.arsan.ai.auth.model.ResetPasswordRequest;

public interface PasswordService {

    void forgotPassword(String email);

    void resetPassword(ResetPasswordRequest request);
}
