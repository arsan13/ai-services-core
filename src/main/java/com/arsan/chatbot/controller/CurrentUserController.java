package com.arsan.chatbot.controller;

import com.arsan.chatbot.exception.custom.ResourceNotFoundException;
import com.arsan.chatbot.mapper.UserMapper;
import com.arsan.chatbot.model.common.UserProfile;
import com.arsan.chatbot.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class CurrentUserController {

    private final UserMapper userMapper;

    @GetMapping
    public UserProfile currentUser() {
        return SecurityUtils.getCurrentUser()
                .map(userMapper::toUserProfile)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
