package com.arsan.ai.shared.util;

import com.arsan.ai.auth.enums.PermissionType;
import com.arsan.ai.shared.entity.AppUser;

import java.util.HashSet;
import java.util.Set;

public final class PermissionUtils {

    private PermissionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Set<String> resolvePermissions(AppUser user) {
        if (user == null) {
            return Set.of();
        }

        Set<String> permissions = new HashSet<>();

        permissions.addAll(user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(PermissionType::getValue)
                .toList());

        permissions.addAll(user.getExtraPermissions());

        permissions.removeAll(user.getRevokedPermissions());

        return Set.copyOf(permissions);
    }
}
