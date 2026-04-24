package com.arsan.chatbot.util;

import com.arsan.chatbot.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().map(User::getId).orElse(0L);
    }

    public static Optional<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }

        User user = (User) auth.getPrincipal();
        return Optional.of(user);
    }
}
