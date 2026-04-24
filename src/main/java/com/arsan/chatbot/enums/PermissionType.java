package com.arsan.chatbot.enums;

import lombok.Getter;

@Getter
public enum PermissionType {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete"),
    USER_MANAGE("user:manage"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_DELETE("admin:delete"),
    TOKEN_USAGE_READ("token:usage:read");

    private final String authority;

    PermissionType(String authority) {
        this.authority = authority;
    }
}
