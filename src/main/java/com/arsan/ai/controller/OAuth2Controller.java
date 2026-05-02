package com.arsan.ai.controller;

import com.arsan.ai.enums.AuthProviderType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @GetMapping("/providers")
    public List<String> getProviders() {
        return Arrays.stream(AuthProviderType.values())
                .filter(p -> !p.equals(AuthProviderType.LOCAL))
                .map(p -> p.name().toLowerCase())
                .toList();
    }

    @GetMapping("/{providerType}")
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
