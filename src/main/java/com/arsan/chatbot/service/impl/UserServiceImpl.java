package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.enums.Role;
import com.arsan.chatbot.exception.custom.ResourceNotFoundException;
import com.arsan.chatbot.mapper.UserMapper;
import com.arsan.chatbot.model.user.UserDTO;
import com.arsan.chatbot.repository.UserRepository;
import com.arsan.chatbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Override
    public void updateRole(Long id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }
}
