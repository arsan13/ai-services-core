package com.arsan.ai.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
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

    private static final Map<String, PermissionType> BY_VALUE = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(PermissionType::getValue, p -> p));

    public static final Set<String> VERIFIED_USERS_VALUES = Set.of(CHAT_GENERIC_USE.getValue());
    public static final Set<String> ALL_VALUES = Collections.unmodifiableSet(BY_VALUE.keySet());

    PermissionType(String value) {
        this.value = value;
    }

    public static Optional<PermissionType> fromValue(String value) {
        return Optional.ofNullable(BY_VALUE.get(value));
    }

    public static boolean isValid(String value) {
        return BY_VALUE.containsKey(value);
    }

    public static void validate(String value) {
        if (value == null || !BY_VALUE.containsKey(value)) {
            throw new IllegalArgumentException("Invalid permission: " + value);
        }
    }

    public static void validateAll(Collection<String> values) {
        if (values == null) {
            throw new IllegalArgumentException("Permissions collection cannot be null");
        }
        for (String value : values) {
            validate(value);
        }
    }
}
