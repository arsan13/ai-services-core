package com.arsan.ai.admin.service;

import com.arsan.ai.auth.enums.RoleType;

import java.util.Map;
import java.util.Set;

public interface RoleService {

    Map<RoleType, Set<String>> availableRoles();

    Map<RoleType, Set<String>> availableRoles(Long userId);

    void grantRoles(Long userId, Set<RoleType> roles);

    void revokeRoles(Long userId, Set<RoleType> roles);


}
