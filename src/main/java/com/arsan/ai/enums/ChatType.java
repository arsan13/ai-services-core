package com.arsan.ai.enums;

import lombok.Getter;

@Getter
public enum ChatType {

    AVIATION("aviation", "Aviation Fuel Operations Chat", PermissionType.CHAT_AVIATION_USE),
    GENERIC("generic", "Generic Chat", PermissionType.CHAT_GENERIC_USE);

    private final String code;
    private final String displayName;
    private final PermissionType permission;

    ChatType(String code, String displayName, PermissionType permission) {
        this.code = code;
        this.displayName = displayName;
        this.permission = permission;
    }

    public static ChatType fromCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Chat type must not be null or blank");
        }

        for (ChatType type : ChatType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unsupported chat type: " + code);
    }
}
