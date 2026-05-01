package com.arsan.ai.model.common;

import com.arsan.ai.enums.AuthProviderType;
import com.arsan.ai.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private Long id;
    private String fullName;
    private String username;
    private Set<RoleType> roles;
    private Set<String> permissions;
    private AuthProviderType providerType;
}
