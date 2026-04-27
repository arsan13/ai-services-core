package com.arsan.chatbot.util;

import com.arsan.chatbot.entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(User::getId);
    }

    public static Optional<User> getCurrentUser() {
        return Optional.ofNullable(getAuthentication())
                .filter(Authentication::isAuthenticated)
                .filter(auth -> !(auth instanceof AnonymousAuthenticationToken))
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(User.class::cast);
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
