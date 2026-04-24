package com.arsan.chatbot.model.user;

import com.arsan.chatbot.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequest {
    private RoleType role;
}
