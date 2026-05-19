package com.arsan.ai.identity.events;

import com.arsan.ai.identity.entity.AppUser;

public record UserUpdatedEvent(AppUser user) {
}
