package com.arsan.ai.auth.service;

import com.arsan.ai.auth.model.ChangePasswordRequest;
import com.arsan.ai.auth.model.ResetPasswordRequest;

public interface PasswordService {

    void changePassword(ChangePasswordRequest request);

    void forgotPassword(String email);

    void resetPassword(ResetPasswordRequest request);
}
