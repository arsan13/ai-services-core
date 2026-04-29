package com.arsan.ai.projection;

import com.arsan.ai.enums.PermissionType;
import com.arsan.ai.enums.RoleType;

import java.time.LocalDateTime;
import java.util.List;

public interface UserResponse {
    Long getId();

    String getFullName();

    String getUsername();

    List<RoleType> getRoles();

    List<PermissionType> getPermissions();

    LocalDateTime getCreatedDate();
}