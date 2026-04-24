package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.enums.RoleType;
import com.arsan.chatbot.exception.custom.ResourceNotFoundException;
import com.arsan.chatbot.projection.UserResponse;
import com.arsan.chatbot.repository.UserRepository;
import com.arsan.chatbot.service.UserService;
import jakarta.transaction.Transactional;
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
        return userRepository.findById(id, UserResponse.class).orElseThrow(this::userNotFound);
    }

    @Override
    @Transactional
    public void makeAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(this::userNotFound);

        if (user.getRoles().contains(RoleType.ROLE_ADMIN)) {
            throw new IllegalArgumentException("User is already an admin");
        }

        user.getRoles().add(RoleType.ROLE_ADMIN);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void revokeAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(this::userNotFound);

        if (!user.getRoles().contains(RoleType.ROLE_ADMIN)) {
            throw new IllegalArgumentException("User is not an admin");
        }

        user.getRoles().remove(RoleType.ROLE_ADMIN);
        userRepository.save(user);
    }

    private ResourceNotFoundException userNotFound() {
        return new ResourceNotFoundException("User not found");
    }
}
