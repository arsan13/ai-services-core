package com.arsan.ai.auth.events;

import com.arsan.ai.identity.entity.AppUser;

public record PasswordResetRequestedEvent(AppUser user) {
}
