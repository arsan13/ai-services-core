package com.arsan.ai.identity.events;

import com.arsan.ai.identity.model.UserEvictDto;

public record UserUpdatedEvent(UserEvictDto userEvictDto) {
}
