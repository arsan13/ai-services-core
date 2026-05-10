package com.arsan.ai.admin.service;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    void grantPermission(Long userId, Set<String> permissions);

    void revokePermission(Long userId, Set<String> permissions);

    List<String> availablePermissions(Long userId);

    List<String> availablePermissions();

}
