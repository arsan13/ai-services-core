package com.arsan.ai.enums;

import lombok.Getter;

@Getter
public enum ChatType {
    AVIATION("aviation", "Aviation Fuel Operations Chat"),
    GENERIC("generic", "Generic Chat");

    private final String code;
    private final String displayName;

    ChatType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public static ChatType fromCode(String code) {
        if (code == null) {
            return GENERIC;
        }
        for (ChatType type : ChatType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return GENERIC;
    }
}
