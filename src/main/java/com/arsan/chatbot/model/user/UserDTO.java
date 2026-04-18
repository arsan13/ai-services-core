package com.arsan.chatbot.model.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
}
