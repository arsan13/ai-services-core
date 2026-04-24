package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.enums.PermissionType;
import com.arsan.chatbot.enums.RoleType;
import com.arsan.chatbot.exception.custom.ResourceNotFoundException;
import com.arsan.chatbot.projection.UserResponse;
import com.arsan.chatbot.repository.UserRepository;
import com.arsan.chatbot.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return userRepository.findById(id, UserResponse.class).orElseThrow(this::userNotFound);
    }

    @Override
    @Transactional
    public void grantRole(Long id, RoleType role) {
        User user = userRepository.findById(id).orElseThrow(this::userNotFound);

        if (user.getRoles().contains(role)) {
            throw new IllegalArgumentException("User already has this role");
        }

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void revokeRole(Long id, RoleType role) {
        User user = userRepository.findById(id).orElseThrow(this::userNotFound);

        if (!user.getRoles().contains(role)) {
            throw new IllegalArgumentException("User does not have this role");
        }

        user.getRoles().remove(role);
        userRepository.save(user);
    }

    @Override
    public List<PermissionType> availablePermissions() {
        return List.of(PermissionType.values());
    }

    @Transactional
    public void grantPermission(Long id, List<PermissionType> permissions) {
        User user = userRepository.findById(id).orElseThrow(this::userNotFound);

        permissions.stream()
                .filter(permission -> !user.getPermissions().contains(permission))
                .forEach(permission -> user.getPermissions().add(permission));

        userRepository.save(user);
    }

    @Transactional
    public void revokePermission(Long id, List<PermissionType> permissions) {
        User user = userRepository.findById(id).orElseThrow(this::userNotFound);

        permissions.stream()
                .filter(permission -> user.getPermissions().contains(permission))
                .forEach(permission -> user.getPermissions().remove(permission));

        userRepository.save(user);
    }

    private ResourceNotFoundException userNotFound() {
        return new ResourceNotFoundException("User not found");
    }
}
