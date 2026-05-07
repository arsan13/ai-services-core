package com.arsan.ai.service.user.impl;

import com.arsan.ai.entity.AppUser;
import com.arsan.ai.enums.PermissionType;
import com.arsan.ai.enums.RoleType;
import com.arsan.ai.projection.UserResponse;
import com.arsan.ai.repository.UserRepository;
import com.arsan.ai.service.user.UserService;
import com.arsan.ai.util.ExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

        AppUser user = getUserOrThrow(id);

        permissions.stream()
                .filter(permission -> !user.getPermissions().contains(permission))
                .forEach(user.getPermissions()::add);
    }

    @Transactional
    public void revokePermission(Long id, List<String> permissions) {
        PermissionType.validateAll(permissions);

        AppUser user = getUserOrThrow(id);

        permissions.stream()
                .filter(user.getPermissions()::contains)
                .forEach(user.getPermissions()::remove);
    }

    private @NonNull AppUser getUserOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(ExceptionUtils::userNotFound);
    }
}
