package com.arsan.ai.controller;


import com.arsan.ai.model.auth.AuthRequest;
import com.arsan.ai.model.auth.AuthResponse;
import com.arsan.ai.model.auth.AvailabilityResponse;
import com.arsan.ai.model.auth.EmailRequest;
import com.arsan.ai.model.auth.RegisterRequest;
import com.arsan.ai.model.auth.ResetPasswordRequest;
import com.arsan.ai.model.auth.TokenRequest;
import com.arsan.ai.service.AuthService;
import com.arsan.ai.service.EmailVerificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/availability")
    public AvailabilityResponse check(@RequestParam @Email String email) {
        return authService.isEmailAvailable(email);
    }

    @PostMapping("/verify-email")
    public void verifyEmail(@RequestBody @Valid TokenRequest request) {
        emailVerificationService.verify(request.getToken());
    }

    @PostMapping("/resend-verification")
    public void resendVerificationEmail(@RequestBody @Valid EmailRequest request) {
        emailVerificationService.resendVerificationEmail(request.getEmail());
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody @Valid EmailRequest request) {
        authService.forgotPassword(request.getEmail());
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
    }
}
