package com.arsan.ai.admin.service.impl;

import com.arsan.ai.admin.service.RoleService;
import com.arsan.ai.auth.enums.PermissionType;
import com.arsan.ai.auth.enums.RoleType;
import com.arsan.ai.shared.cache.AppUserCache;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.events.UserUpdatedEvent;
import com.arsan.ai.shared.repository.UserRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final AppUserCache userCache;

    @Override
    public Map<RoleType, Set<String>> availableRoles(Long userId) {
        AppUser user = userCache.getById(userId);

        Map<RoleType, Set<String>> map = availableRoles();
        map.keySet().removeAll(user.getRoles());

        return map;
    }

    @Override
    public Map<RoleType, Set<String>> availableRoles() {
        Map<RoleType, Set<String>> map = new EnumMap<>(RoleType.class);

        for (RoleType role : RoleType.values()) {
            map.put(role, role.getPermissions().stream().map(PermissionType::getValue).collect(Collectors.toSet()));
        }

        return map;
    }

    @Override
    @Transactional
    public void grantRoles(Long userId, Set<RoleType> roles) {
        if (roles == null || roles.isEmpty()) {
            return;
        }

        AppUser user = userRepository.findById(userId).orElseThrow(ExceptionUtils::userNotFound);
        user.getRoles().addAll(roles);

        eventPublisher.publishEvent(new UserUpdatedEvent(user));
    }

    @Override
    @Transactional
    public void revokeRoles(Long userId, Set<RoleType> roles) {
        if (roles == null || roles.isEmpty()) {
            return;
        }

        AppUser user = userRepository.findById(userId).orElseThrow(ExceptionUtils::userNotFound);
        user.getRoles().removeAll(new HashSet<>(roles));

        eventPublisher.publishEvent(new UserUpdatedEvent(user));
    }
}
