package com.arsan.ai.shared.cache;

import com.arsan.ai.shared.entity.AccessRequest;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import com.arsan.ai.shared.repository.AccessRequestRepository;
import com.arsan.ai.shared.repository.projection.PendingAccessRequestProjection;
import com.arsan.ai.shared.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccessRequestCache {

    public static final String REQUEST_BY_ID_CACHE = "accessRequestById";
    public static final String PENDING_BY_USER_CACHE = "accessRequestPendingByUser";
    public static final String STATUS_PAGE_CACHE = "accessRequestStatusPage";
    public static final String ALL_PAGE_CACHE = "accessRequestAllPage";
    public static final String ADMIN_STATUS_PAGE_CACHE = "accessRequestAdminStatusPage";
    public static final String ADMIN_ALL_PAGE_CACHE = "accessRequestAdminAllPage";

    private final AccessRequestRepository accessRequestRepository;

    @Cacheable(value = REQUEST_BY_ID_CACHE, key = "#requestId + ':' + #requesterId")
    public AccessRequest getByIdAndRequester(Long requestId, Long requesterId) {
        return accessRequestRepository
                .findByIdAndRequesterId(requestId, requesterId)
                .orElseThrow(ExceptionUtils::resourceNotFound);
    }

    @Cacheable(value = PENDING_BY_USER_CACHE, key = "#userId")
    public List<PendingAccessRequestProjection> getPendingByUser(Long userId) {
        return accessRequestRepository.findByStatusAndRequesterId(AccessRequestStatus.PENDING, userId, PendingAccessRequestProjection.class);
    }

    @Cacheable(value = STATUS_PAGE_CACHE, key = "#status.name() + ':' + #userId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()")
    public Page<AccessRequest> getByStatusAndRequester(AccessRequestStatus status, Long userId, Pageable pageable) {
        return accessRequestRepository.findByStatusAndRequesterId(status, userId, pageable);
    }

    @Cacheable(value = ALL_PAGE_CACHE, key = "#userId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()")
    public Page<AccessRequest> getAllByRequester(Long userId, Pageable pageable) {
        return accessRequestRepository.findByRequesterId(userId, pageable);
    }

    @Cacheable(value = ADMIN_STATUS_PAGE_CACHE, key = "#status.name() + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()")
    public Page<AccessRequest> getByStatus(AccessRequestStatus status, Pageable pageable) {
        return accessRequestRepository.findByStatus(status, pageable);
    }

    @Cacheable(value = ADMIN_ALL_PAGE_CACHE, key = "#pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()")
    public Page<AccessRequest> getAll(Pageable pageable) {
        return accessRequestRepository.findAll(pageable);
    }

    @Caching(evict = {
            @CacheEvict(value = REQUEST_BY_ID_CACHE, key = "#request.id + ':' + #request.requester.id"),
            @CacheEvict(value = PENDING_BY_USER_CACHE, key = "#request.requester.id"),
            @CacheEvict(value = STATUS_PAGE_CACHE, allEntries = true),
            @CacheEvict(value = ALL_PAGE_CACHE, allEntries = true)
    })
    public void evict(AccessRequest request) {
        // declarative eviction; intentionally left blank
        Objects.requireNonNull(request);
    }
}
