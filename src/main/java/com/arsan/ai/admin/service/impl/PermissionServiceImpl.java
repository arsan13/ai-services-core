package com.arsan.ai.admin.service.impl;

import com.arsan.ai.admin.service.PermissionService;
import com.arsan.ai.auth.enums.PermissionType;
import com.arsan.ai.auth.enums.RoleType;
import com.arsan.ai.shared.cache.AppUserCache;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.events.UserUpdatedEvent;
import com.arsan.ai.shared.repository.UserRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import com.arsan.ai.shared.util.PermissionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final AppUserCache userCache;

    public List<String> availablePermissions() {
        return Arrays.stream(PermissionType.values())
                .map(PermissionType::getValue)
                .toList();
    }

    @Override
    public List<String> availablePermissions(Long userId) {
        AppUser user = userCache.getById(userId);
        Set<String> existingPermissions = PermissionUtils.resolvePermissions(user);

        return availablePermissions().stream()
                .filter(p -> !existingPermissions.contains(p))
                .toList();
    }

    @Override
    public List<String> availablePermissions(RoleType roleType) {
        return roleType.getPermissions().stream()
                .map(PermissionType::getValue)
                .toList();
    }

    @Transactional
    public void grantPermission(Long userId, Set<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }
        PermissionType.validateAll(permissions);
        AppUser user = userRepository.findById(userId).orElseThrow(ExceptionUtils::userNotFound);

        permissions.stream()
                .filter(p -> !user.getExtraPermissions().contains(p))
                .forEach(user.getExtraPermissions()::add);

        // If previously revoked, remove from revoked list
        user.getRevokedPermissions().removeAll(new HashSet<>(permissions));

        eventPublisher.publishEvent(new UserUpdatedEvent(user));
    }

    @Transactional
    public void revokePermission(Long userId, Set<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }
        PermissionType.validateAll(permissions);
        AppUser user = userRepository.findById(userId).orElseThrow(ExceptionUtils::userNotFound);

        permissions.stream()
                .filter(p -> !user.getRevokedPermissions().contains(p))
                .forEach(user.getRevokedPermissions()::add);

        // If previously granted as extra, remove it
        user.getExtraPermissions().removeAll(new HashSet<>(permissions));

        eventPublisher.publishEvent(new UserUpdatedEvent(user));
    }
}
