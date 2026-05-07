package com.arsan.ai.auth.service;

import com.arsan.ai.auth.model.AuthRequest;
import com.arsan.ai.auth.model.AuthResponse;
import com.arsan.ai.auth.model.AvailabilityResponse;
import com.arsan.ai.auth.model.RegisterRequest;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    AuthResponse register(RegisterRequest authRequest);

    AvailabilityResponse isEmailAvailable(String email);

    void resendVerificationEmail(String email);

    void verifyUser(String token);
}
