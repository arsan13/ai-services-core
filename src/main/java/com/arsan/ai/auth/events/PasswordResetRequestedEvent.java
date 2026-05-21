package com.arsan.ai.auth.events;

import com.arsan.ai.identity.model.AppUserDto;

public record PasswordResetRequestedEvent(AppUserDto user) {
}
