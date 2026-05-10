package com.arsan.ai.admin.service;

import com.arsan.ai.admin.projection.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAll();

    UserResponse getUserById(Long id);
}
