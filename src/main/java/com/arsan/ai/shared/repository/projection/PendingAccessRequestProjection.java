package com.arsan.ai.shared.repository.projection;

import com.arsan.ai.auth.enums.RoleType;

import java.util.Set;

public interface PendingAccessRequestProjection {
    Set<RoleType> getRoles();
    Set<String> getPermissions();
}

