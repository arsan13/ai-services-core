package com.arsan.ai.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum PermissionType {

    // Admin
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_DELETE("admin:delete"),

    // User
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete"),
    USER_MANAGE("user:manage"),

    // Token
    TOKEN_USAGE_READ("token:usage:read"),

    // Chat
    CHAT_AVIATION_USE("chat:aviation:use"),
    CHAT_GENERIC_USE("chat:generic:use");

    private final String value;

    public static final Set<String> ALL_VALUES = Arrays.stream(values())
            .map(PermissionType::getValue)
            .collect(Collectors.toUnmodifiableSet());

    PermissionType(String value) {
        this.value = value;
    }

    public static Optional<PermissionType> fromValue(String value) {
        return Arrays.stream(values())
                .filter(p -> p.value.equals(value))
                .findFirst();
    }

    public static void validate(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid permission: " + value);
        }
    }

    public static void validateAll(Collection<String> values) {
        values.forEach(PermissionType::validate);
    }

    public static boolean isValid(String value) {
        return fromValue(value).isPresent();
    }
}
