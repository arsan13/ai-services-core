package com.arsan.chatbot.service;

import com.arsan.chatbot.enums.Role;
import com.arsan.chatbot.model.user.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAll();

    void updateRole(Long id, Role role);

}
