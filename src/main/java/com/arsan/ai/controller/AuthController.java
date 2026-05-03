package com.arsan.ai.controller;


import com.arsan.ai.model.auth.AuthRequest;
import com.arsan.ai.model.auth.AuthResponse;
import com.arsan.ai.model.auth.AvailabilityResponse;
import com.arsan.ai.model.auth.RegisterRequest;
import com.arsan.ai.model.auth.ResetPasswordRequest;
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

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token) {
        emailVerificationService.verify(token);
        return "Email verified. Please login!";
    }

    @GetMapping("/resend-verification")
    public String resendVerificationEmail(@RequestParam @Email String email) {
        emailVerificationService.resendVerificationEmail(email);
        return "Verification email resent. Please check your inbox!";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(@RequestParam @Email String email) {
        authService.forgotPassword(email);
        return "Password reset email sent. Please check your inbox!";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return "Password updated. Please login!";
    }
}
