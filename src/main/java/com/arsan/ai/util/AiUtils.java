package com.arsan.ai.util;

public final class AiUtils {

    private AiUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String getConversationId() {
        Long userId = SecurityUtils.getCurrentUserId().orElse(0L);
        return String.valueOf(userId);
    }
}
