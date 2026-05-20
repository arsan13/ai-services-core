package com.arsan.ai.accessrequest.events;

public record AccessRequestRejectedEvent(
        Long requestId,
        String userEmail,
        String userName,
        String reason
) {
}
