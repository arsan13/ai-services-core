package com.arsan.ai.accessrequest.repository.projection;

import com.arsan.ai.identity.enums.RoleType;

import java.util.Set;

public interface PendingRolesPermissionsProjection {
    Set<RoleType> getRoles();

    Set<String> getPermissions();
}

