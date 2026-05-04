package com.arsan.ai.util;

import com.arsan.ai.entity.AppUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(AppUser::getId);
    }

    public static Optional<AppUser> getCurrentUser() {
        return Optional.ofNullable(getAuthentication())
                .filter(Authentication::isAuthenticated)
                .filter(auth -> !(auth instanceof AnonymousAuthenticationToken))
                .map(Authentication::getPrincipal)
                .filter(AppUser.class::isInstance)
                .map(AppUser.class::cast);
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
