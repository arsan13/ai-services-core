package com.arsan.ai.shared.cache;

import com.arsan.ai.shared.enums.AccessRequestStatus;
import com.arsan.ai.shared.repository.AccessRequestRepository;
import com.arsan.ai.shared.repository.projection.PendingAccessRequestProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessRequestCache {

    public static final String PENDING_BY_USER_CACHE = "accessRequestPendingByUser";

    private final AccessRequestRepository accessRequestRepository;

    @Cacheable(value = PENDING_BY_USER_CACHE, key = "#userId")
    public List<PendingAccessRequestProjection> getPendingByUser(Long userId) {
        return accessRequestRepository.findByStatusAndRequesterId(AccessRequestStatus.PENDING, userId, PendingAccessRequestProjection.class);
    }

    @Caching(evict = {
            @CacheEvict(value = PENDING_BY_USER_CACHE, key = "#requesterId")
    })
    public void evict(Long requesterId) {
        // Intentionally empty: cache eviction is handled entirely by Spring AOP via @CacheEvict annotations.
    }
}
