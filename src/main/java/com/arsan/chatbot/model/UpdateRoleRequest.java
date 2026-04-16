package com.arsan.chatbot.model;

import com.arsan.chatbot.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequest {
    private Role role;
}
