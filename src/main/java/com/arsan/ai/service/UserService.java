package com.arsan.ai.service;

import com.arsan.ai.enums.PermissionType;
import com.arsan.ai.enums.RoleType;
import com.arsan.ai.projection.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAll();

    UserResponse getUserById(Long id);

    void grantRole(Long id, RoleType role);

    void revokeRole(Long id, RoleType role);

    void grantPermission(Long id, List<PermissionType> permissions);

    void revokePermission(Long id, List<PermissionType> permissions);

    List<PermissionType> availablePermissions();
}
