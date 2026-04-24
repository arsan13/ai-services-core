package com.arsan.chatbot.service;

import com.arsan.chatbot.enums.PermissionType;
import com.arsan.chatbot.enums.RoleType;
import com.arsan.chatbot.projection.UserResponse;

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
