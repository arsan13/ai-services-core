package com.arsan.chatbot.projection;

import com.arsan.chatbot.enums.RoleType;

public interface UserResponse {
    Long getId();
    String getFullName();
    String getEmail();
    String getUsername();
    RoleType getRole();
}