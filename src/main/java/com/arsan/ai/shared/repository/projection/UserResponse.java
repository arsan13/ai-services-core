package com.arsan.ai.shared.repository.projection;

import com.arsan.ai.auth.enums.RoleType;

import java.time.LocalDateTime;
import java.util.List;

public interface UserResponse {
    Long getId();

    String getFullName();

    String getEmail();

    List<RoleType> getRoles();

    List<String> getExtraPermissions();

    List<String> getRevokedPermissions();

    boolean isVerified();

    LocalDateTime getCreatedDate();

    LocalDateTime getUpdatedDate();

    LocalDateTime getPasswordResetDate();
}