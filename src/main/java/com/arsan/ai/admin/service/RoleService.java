package com.arsan.ai.admin.service;

import com.arsan.ai.auth.enums.RoleType;

import java.util.List;
import java.util.Set;

public interface RoleService {

    List<RoleType> availableRoles();

    List<RoleType> availableRoles(Long userId);

    void grantRoles(Long userId, Set<RoleType> roles);

    void revokeRoles(Long userId, Set<RoleType> roles);


}
