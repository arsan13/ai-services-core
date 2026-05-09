package com.arsan.ai.shared.util;

import com.arsan.ai.auth.enums.PermissionType;
import com.arsan.ai.shared.entity.AppUser;

import java.util.Set;
import java.util.stream.Collectors;

public final class PermissionUtils {

    private PermissionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Set<String> resolvePermissions(AppUser user) {
        if (user == null) {
            return Set.of();
        }

        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(PermissionType::getValue)
                .collect(Collectors.toSet());

        permissions.addAll(user.getExtraPermissions());
        permissions.removeAll(user.getRevokedPermissions());

        return Set.copyOf(permissions);
    }
}
