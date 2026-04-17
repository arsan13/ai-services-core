package com.arsan.chatbot.service;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.enums.Role;

import java.util.List;

public interface UserService {

    List<User> getAll();

    void updateRole(Long id, Role role);

}
