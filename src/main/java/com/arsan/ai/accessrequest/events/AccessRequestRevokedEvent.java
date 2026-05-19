package com.arsan.ai.accessrequest.events;

public record AccessRequestRevokedEvent(
        Long requestId,
        String userEmail,
        String userName,
        String reason
) {
}
