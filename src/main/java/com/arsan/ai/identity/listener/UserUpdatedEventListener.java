package com.arsan.ai.identity.listener;

import com.arsan.ai.identity.cache.AppUserCache;
import com.arsan.ai.identity.events.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class UserUpdatedEventListener {

    private final AppUserCache userCache;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handle(UserUpdatedEvent event) {
        userCache.evict(event.user());
    }
}
