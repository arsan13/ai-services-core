package com.arsan.ai.events;

import com.arsan.ai.entity.AppUser;

public record EmailVerificationRequestedEvent(AppUser user) {
}
