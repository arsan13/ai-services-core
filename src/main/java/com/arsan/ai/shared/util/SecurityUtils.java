package com.arsan.ai.shared.util;

import com.arsan.ai.identity.model.AppUserDto;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(AppUserDto::getId);
    }

    public static Long getCurrentUserIdOrThrow() {
        return getCurrentUserId().orElseThrow(ExceptionUtils::userNotFound);
    }

    public static Optional<AppUserDto> getCurrentUser() {
        return Optional.ofNullable(getAuthentication())
                .filter(Authentication::isAuthenticated)
                .filter(auth -> !(auth instanceof AnonymousAuthenticationToken))
                .map(Authentication::getPrincipal)
                .filter(AppUserDto.class::isInstance)
                .map(AppUserDto.class::cast);
    }

    public static AppUserDto getCurrentUserOrThrow() {
        return getCurrentUser().orElseThrow(ExceptionUtils::userNotFound);
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
