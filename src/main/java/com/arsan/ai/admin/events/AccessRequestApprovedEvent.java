package com.arsan.ai.admin.events;

public record AccessRequestApprovedEvent(
        Long requestId,
        String userEmail,
        String userName
) {
}
