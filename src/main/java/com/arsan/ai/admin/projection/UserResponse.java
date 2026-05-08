package com.arsan.ai.admin.projection;

import com.arsan.ai.auth.enums.RoleType;

import java.time.LocalDateTime;
import java.util.List;

public interface UserResponse {
    Long getId();

    String getFullName();

    String getEmail();

    List<RoleType> getRoles();

    List<String> getPermissions();

    boolean isVerified();

    LocalDateTime getCreatedDate();

    LocalDateTime getUpdatedDate();

    LocalDateTime getPasswordResetDate();
}