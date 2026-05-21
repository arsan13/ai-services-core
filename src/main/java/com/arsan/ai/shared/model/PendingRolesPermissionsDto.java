package com.arsan.ai.shared.model;

import com.arsan.ai.identity.enums.RoleType;

import java.util.Set;

public record PendingRolesPermissionsDto(Set<RoleType> roles, Set<String> permissions) {
}
