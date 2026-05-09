package com.arsan.ai.auth.enums;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum RoleType {

    ROLE_USER(
            Set.of(
                    PermissionType.USER_READ,
                    PermissionType.REQUEST_ACCESS_CREATE
            )
    ),

    ROLE_ANALYST(
            Set.of(
                    PermissionType.TOKEN_USAGE_READ
            ),
            ROLE_USER
    ),

    ROLE_MANAGER(
            Set.of(
                    PermissionType.ADMIN_READ,
                    PermissionType.REQUEST_ACCESS_VIEW
            ),
            ROLE_USER
    ),

    ROLE_ADMIN(
            Set.of(
                    PermissionType.ADMIN_WRITE,
                    PermissionType.USER_WRITE,
                    PermissionType.USER_DELETE,
                    PermissionType.CHAT_AVIATION_USE,
                    PermissionType.CHAT_GENERIC_USE,
                    PermissionType.REQUEST_ACCESS_APPROVE
            ),
            ROLE_ANALYST,
            ROLE_MANAGER
    );

    private final Set<PermissionType> permissions;

    RoleType(Set<PermissionType> directPermissions, RoleType... inheritedRoles) {
        Set<PermissionType> allPermissions = new HashSet<>(directPermissions);

        for (RoleType role : inheritedRoles) {
            allPermissions.addAll(role.getPermissions());
        }

        this.permissions = Set.copyOf(allPermissions);
    }
}
