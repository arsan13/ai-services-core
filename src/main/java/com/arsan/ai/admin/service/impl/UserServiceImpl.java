package com.arsan.ai.admin.service.impl;

import com.arsan.ai.admin.projection.UserResponse;
import com.arsan.ai.admin.service.UserService;
import com.arsan.ai.auth.enums.PermissionType;
import com.arsan.ai.auth.enums.RoleType;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.repository.UserRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAllBy(UserResponse.class);
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id, UserResponse.class).orElseThrow(ExceptionUtils::userNotFound);
    }

    @Override
    @Transactional
    public void grantRole(Long id, RoleType role) {
        AppUser user = getUserOrThrow(id);

        if (user.getRoles().contains(role)) {
            throw new IllegalArgumentException("User already has this role");
        }

        user.getRoles().add(role);
    }

    @Override
    @Transactional
    public void revokeRole(Long id, RoleType role) {
        AppUser user = getUserOrThrow(id);

        if (!user.getRoles().contains(role)) {
            throw new IllegalArgumentException("User does not have this role");
        }

        user.getRoles().remove(role);
    }

    public List<String> availablePermissions() {
        return Arrays.stream(PermissionType.values())
                .map(PermissionType::getValue)
                .toList();
    }

    @Transactional
    public void grantPermission(Long id, List<String> permissions) {
        PermissionType.validateAll(permissions);

        AppUser user = userRepository.findById(id).orElseThrow(ExceptionUtils::userNotFound);

        permissions.stream()
                .filter(p -> !user.getExtraPermissions().contains(p))
                .forEach(user.getExtraPermissions()::add);

        // If previously revoked, remove from revoked list
        user.getRevokedPermissions().removeAll(new HashSet<>(permissions));
    }

    @Transactional
    public void revokePermission(Long id, List<String> permissions) {
        PermissionType.validateAll(permissions);

        AppUser user = userRepository.findById(id).orElseThrow(ExceptionUtils::userNotFound);

        permissions.stream()
                .filter(p -> !user.getRevokedPermissions().contains(p))
                .forEach(user.getRevokedPermissions()::add);

        // If previously granted as extra, remove it
        user.getExtraPermissions().removeAll(new HashSet<>(permissions));
    }


    private @NonNull AppUser getUserOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(ExceptionUtils::userNotFound);
    }
}
