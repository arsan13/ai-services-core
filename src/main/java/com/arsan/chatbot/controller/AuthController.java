package com.arsan.chatbot.controller;


import com.arsan.chatbot.model.auth.AuthRequest;
import com.arsan.chatbot.model.auth.AuthResponse;
import com.arsan.chatbot.model.auth.AvailabilityResponse;
import com.arsan.chatbot.model.auth.RegisterRequest;
import com.arsan.chatbot.service.AuthService;
import jakarta.validation.Valid;
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

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/availability")
    public AvailabilityResponse check(@RequestParam String username) {
        return authService.isUsernameAvailable(username);
    }
}
