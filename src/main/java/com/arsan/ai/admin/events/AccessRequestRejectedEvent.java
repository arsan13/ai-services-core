package com.arsan.ai.admin.events;

public record AccessRequestRejectedEvent(
        Long requestId,
        String userEmail,
        String userName,
        String reason
) {
}
