package com.arsan.ai.enums;

import lombok.Getter;

@Getter
public enum ChatType {

    GENERIC("generic", "Generic", PermissionType.CHAT_GENERIC_USE.getValue()),
    AVIATION("aviation", "Aviation", PermissionType.CHAT_AVIATION_USE.getValue());

    private final String code;
    private final String displayName;
    private final String permission;

    ChatType(String code, String displayName, String permission) {
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
