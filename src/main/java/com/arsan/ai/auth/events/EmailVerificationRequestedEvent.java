package com.arsan.ai.auth.events;

import com.arsan.ai.shared.entity.AppUser;

public record EmailVerificationRequestedEvent(AppUser user) {
}
