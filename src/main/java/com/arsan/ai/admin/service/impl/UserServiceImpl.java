package com.arsan.ai.admin.service.impl;

import com.arsan.ai.shared.repository.projection.UserResponse;
import com.arsan.ai.admin.service.UserService;
import com.arsan.ai.shared.repository.UserRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAllBy(UserResponse.class);
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id, UserResponse.class).orElseThrow(ExceptionUtils::userNotFound);
    }
}
