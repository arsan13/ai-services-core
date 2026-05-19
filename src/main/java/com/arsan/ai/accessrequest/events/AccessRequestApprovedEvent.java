package com.arsan.ai.accessrequest.events;

public record AccessRequestApprovedEvent(
        Long requestId,
        String userEmail,
        String userName
) {
}
