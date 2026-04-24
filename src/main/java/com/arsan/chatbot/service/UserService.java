package com.arsan.chatbot.service;

import com.arsan.chatbot.projection.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAll();

    UserResponse getUserById(Long id);

    void makeAdmin(Long id);

    void revokeAdmin(Long id);

}
