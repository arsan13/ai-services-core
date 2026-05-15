package com.arsan.ai.profile.model;

import com.arsan.ai.auth.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingRolesPermissionsDto {

    private Set<RoleType> roles;
    private Set<String> permissions;
}
