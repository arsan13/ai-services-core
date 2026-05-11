package com.arsan.ai.admin.service;

import com.arsan.ai.auth.enums.RoleType;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    List<String> availablePermissions();

    List<String> availablePermissions(Long userId);

    List<String> availablePermissions(RoleType role);

    void grantPermission(Long userId, Set<String> permissions);

    void revokePermission(Long userId, Set<String> permissions);
}
