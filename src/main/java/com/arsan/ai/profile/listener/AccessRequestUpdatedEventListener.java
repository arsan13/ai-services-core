package com.arsan.ai.profile.listener;

import com.arsan.ai.shared.cache.AccessRequestCache;
import com.arsan.ai.shared.events.AccessRequestUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class AccessRequestUpdatedEventListener {

    private final AccessRequestCache requestCache;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handle(AccessRequestUpdatedEvent event) {
        requestCache.evict(event.requesterId());
    }
}

