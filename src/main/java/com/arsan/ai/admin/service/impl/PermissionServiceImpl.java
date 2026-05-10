package com.arsan.ai.admin.service.impl;

import com.arsan.ai.admin.service.PermissionService;
import com.arsan.ai.auth.enums.PermissionType;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.repository.UserRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import com.arsan.ai.shared.util.PermissionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final UserRepository userRepository;

    @Transactional
    public void grantPermission(Long userId, Set<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }
        PermissionType.validateAll(permissions);
        AppUser user = getUserOrThrow(userId);

        permissions.stream()
                .filter(p -> !user.getExtraPermissions().contains(p))
                .forEach(user.getExtraPermissions()::add);

        // If previously revoked, remove from revoked list
        user.getRevokedPermissions().removeAll(new HashSet<>(permissions));
    }

    @Transactional
    public void revokePermission(Long userId, Set<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }
        PermissionType.validateAll(permissions);
        AppUser user = getUserOrThrow(userId);

        permissions.stream()
                .filter(p -> !user.getRevokedPermissions().contains(p))
                .forEach(user.getRevokedPermissions()::add);

        // If previously granted as extra, remove it
        user.getExtraPermissions().removeAll(new HashSet<>(permissions));
    }

    @Override
    public List<String> availablePermissions(Long userId) {
        AppUser user = getUserOrThrow(userId);
        Set<String> existingPermissions = PermissionUtils.resolvePermissions(user);

        return availablePermissions().stream()
                .filter(p -> !existingPermissions.contains(p))
                .toList();
    }

    public List<String> availablePermissions() {
        return Arrays.stream(PermissionType.values())
                .map(PermissionType::getValue)
                .toList();
    }

    private AppUser getUserOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(ExceptionUtils::userNotFound);
    }
}
