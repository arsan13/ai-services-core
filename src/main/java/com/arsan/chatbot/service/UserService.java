package com.arsan.chatbot.service;

import com.arsan.chatbot.enums.Role;

public interface UserService {

    void updateRole(Long id, Role role);
}
