package com.arsan.chatbot.projection;

import com.arsan.chatbot.enums.PermissionType;
import com.arsan.chatbot.enums.RoleType;

import java.util.List;

public interface UserResponse {
    Long getId();
    String getFullName();
    String getUsername();
    List<RoleType> getRoles();
    List<PermissionType> getPermissions();
}