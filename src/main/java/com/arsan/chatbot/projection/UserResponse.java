package com.arsan.chatbot.projection;

import com.arsan.chatbot.enums.Role;

public interface UserResponse {
    Long getId();

    String getFullName();

    String getEmail();

    String getUsername();

    Role getRole();
}