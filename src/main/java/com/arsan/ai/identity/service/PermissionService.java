package com.arsan.ai.identity.service;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    List<String> availablePermissions();

    List<String> availablePermissions(Long userId);

    void grantPermission(Long userId, Set<String> permissions);

    void revokePermission(Long userId, Set<String> permissions);
}
