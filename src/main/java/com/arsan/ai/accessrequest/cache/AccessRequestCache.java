package com.arsan.ai.accessrequest.cache;

import com.arsan.ai.accessrequest.enums.AccessRequestStatus;
import com.arsan.ai.accessrequest.repository.AccessRequestRepository;
import com.arsan.ai.accessrequest.repository.projection.PendingRolesPermissionsProjection;
import com.arsan.ai.identity.enums.RoleType;
import com.arsan.ai.shared.model.PendingRolesPermissionsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccessRequestCache {

    public static final String PENDING_BY_USER_CACHE = "accessRequestPendingByUser";

    private final AccessRequestRepository accessRequestRepository;

    @Cacheable(value = PENDING_BY_USER_CACHE, key = "#userId", sync = true)
    public PendingRolesPermissionsDto getPendingByUser(Long userId) {
        Set<RoleType> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        accessRequestRepository
                .findByStatusAndRequesterId(AccessRequestStatus.PENDING, userId, PendingRolesPermissionsProjection.class)
                .forEach(projection -> {
                    roles.addAll(projection.getRoles());
                    permissions.addAll(projection.getPermissions());
                });

        return new PendingRolesPermissionsDto(roles, permissions);
    }

    @CacheEvict(value = PENDING_BY_USER_CACHE, key = "#requesterId")
    public void evict(Long requesterId) {
        // Intentionally empty: handled by Spring cache AOP.
    }
}
