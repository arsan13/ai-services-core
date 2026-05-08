package com.arsan.ai.auth.enums;

import lombok.Getter;

import java.util.Set;

@Getter
public enum RoleType {

    ROLE_USER(Set.of(
            PermissionType.USER_READ
    )),

    ROLE_ADMIN(Set.of(
            PermissionType.ADMIN_READ,
            PermissionType.ADMIN_WRITE,
            PermissionType.USER_WRITE,
            PermissionType.USER_DELETE,
            PermissionType.USER_MANAGE,
            PermissionType.TOKEN_USAGE_READ,
            PermissionType.CHAT_AVIATION_USE,
            PermissionType.CHAT_GENERIC_USE
    ));

    private final Set<PermissionType> permissions;

    RoleType(Set<PermissionType> permissions) {
        this.permissions = permissions;
    }

}
