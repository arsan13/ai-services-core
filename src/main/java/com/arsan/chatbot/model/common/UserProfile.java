package com.arsan.chatbot.model.common;

import com.arsan.chatbot.enums.AuthProviderType;
import com.arsan.chatbot.enums.PermissionType;
import com.arsan.chatbot.enums.RoleType;
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
    private Set<PermissionType> permissions;
    private AuthProviderType providerType;
}
