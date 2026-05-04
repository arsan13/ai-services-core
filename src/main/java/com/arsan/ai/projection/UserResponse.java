package com.arsan.ai.projection;

import com.arsan.ai.enums.RoleType;

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
}