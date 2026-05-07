package com.arsan.ai.service.auth;

import com.arsan.ai.model.auth.AuthRequest;
import com.arsan.ai.model.auth.AuthResponse;
import com.arsan.ai.model.auth.AvailabilityResponse;
import com.arsan.ai.model.auth.RegisterRequest;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    AuthResponse register(RegisterRequest authRequest);

    AvailabilityResponse isEmailAvailable(String email);

    void resendVerificationEmail(String email);

    void verifyUser(String token);
}
