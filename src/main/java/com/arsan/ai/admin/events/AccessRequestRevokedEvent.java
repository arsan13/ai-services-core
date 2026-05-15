package com.arsan.ai.admin.events;

public record AccessRequestRevokedEvent(
        Long requestId,
        String userEmail,
        String userName,
        String reason
) {
}
