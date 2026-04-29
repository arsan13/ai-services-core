package com.arsan.ai.controller;

import com.arsan.ai.exception.custom.ResourceNotFoundException;
import com.arsan.ai.mapper.UserMapper;
import com.arsan.ai.model.common.UserProfile;
import com.arsan.ai.util.SecurityUtils;
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
