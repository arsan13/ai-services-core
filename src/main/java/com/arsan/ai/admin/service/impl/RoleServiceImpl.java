package com.arsan.ai.admin.service.impl;

import com.arsan.ai.admin.service.RoleService;
import com.arsan.ai.auth.enums.RoleType;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.repository.UserRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void grantRoles(Long userId, Set<RoleType> roles) {
        if (roles == null || roles.isEmpty()) {
            return;
        }

        AppUser user = getUserOrThrow(userId);
        user.getRoles().addAll(roles);
    }

    @Override
    @Transactional
    public void revokeRoles(Long userId, Set<RoleType> roles) {
        if (roles == null || roles.isEmpty()) {
            return;
        }

        AppUser user = getUserOrThrow(userId);
        user.getRoles().removeAll(new HashSet<>(roles));
    }

    @Override
    public List<RoleType> availableRoles(Long userId) {
        AppUser user = getUserOrThrow(userId);
        return availableRoles().stream()
                .filter(roleType -> !user.getRoles().contains(roleType))
                .toList();
    }

    @Override
    public List<RoleType> availableRoles() {
        return List.of(RoleType.values());
    }

    private AppUser getUserOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(ExceptionUtils::userNotFound);
    }
}
