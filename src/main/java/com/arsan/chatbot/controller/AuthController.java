package com.arsan.chatbot.controller;


import com.arsan.chatbot.enums.AuthProviderType;
import com.arsan.chatbot.model.auth.AuthRequest;
import com.arsan.chatbot.model.auth.AuthResponse;
import com.arsan.chatbot.model.auth.AvailabilityResponse;
import com.arsan.chatbot.model.auth.RegisterRequest;
import com.arsan.chatbot.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

    @GetMapping("/oauth2/providers")
    public List<String> getProviders() {
        return Arrays.stream(AuthProviderType.values())
                .filter(p -> !p.equals(AuthProviderType.LOCAL))
                .map(p -> p.name().toLowerCase())
                .toList();
    }

    @GetMapping("/oauth2/{providerType}")
    public void redirectToProvider(
            @PathVariable String providerType,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        AuthProviderType authProviderType;

        try {
            authProviderType = AuthProviderType.valueOf(providerType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Unsupported auth provider: " + providerType);
        }

        if (authProviderType == AuthProviderType.LOCAL) {
            throw new BadRequestException("Unsupported auth provider: " + providerType);
        }

        String redirectUrl = request.getContextPath() + "/oauth2/authorization/" + providerType.toLowerCase();
        response.sendRedirect(redirectUrl);
    }
}
