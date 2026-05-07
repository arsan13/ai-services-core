package com.arsan.ai.service;

import com.arsan.ai.model.auth.ChangePasswordRequest;
import com.arsan.ai.model.auth.ResetPasswordRequest;

public interface PasswordService {

    void changePassword(ChangePasswordRequest request);

    void forgotPassword(String email);

    void resetPassword(ResetPasswordRequest request);
}
