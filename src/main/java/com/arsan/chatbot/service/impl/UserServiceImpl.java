package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.enums.RoleType;
import com.arsan.chatbot.exception.custom.ResourceNotFoundException;
import com.arsan.chatbot.projection.UserResponse;
import com.arsan.chatbot.repository.UserRepository;
import com.arsan.chatbot.service.UserService;
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
        return userRepository.findById(id, UserResponse.class).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void makeAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRoles().contains(RoleType.ROLE_ADMIN)) {
            throw new IllegalStateException("User is already an admin");
        }

        user.getRoles().add(RoleType.ROLE_ADMIN);
        userRepository.save(user);
    }

    @Override
    public void revokeAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getRoles().contains(RoleType.ROLE_ADMIN)) {
            throw new IllegalStateException("User is not an admin");
        }

        user.getRoles().remove(RoleType.ROLE_ADMIN);
        userRepository.save(user);
    }
}
