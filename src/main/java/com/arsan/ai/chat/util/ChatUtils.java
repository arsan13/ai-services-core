package com.arsan.ai.chat.util;

import com.arsan.ai.shared.util.SecurityUtils;

public final class ChatUtils {

    private ChatUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String getConversationId() {
        Long userId = SecurityUtils.getCurrentUserId().orElse(0L);
        return String.valueOf(userId);
    }
}
