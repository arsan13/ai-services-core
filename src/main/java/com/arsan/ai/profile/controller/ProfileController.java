package com.arsan.ai.profile.controller;

import com.arsan.ai.core.exception.custom.ResourceNotFoundException;
import com.arsan.ai.shared.mapper.UserMapper;
import com.arsan.ai.auth.model.ChangePasswordRequest;
import com.arsan.ai.profile.model.UserProfile;
import com.arsan.ai.auth.service.PasswordService;
import com.arsan.ai.shared.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class ProfileController {

    private final PasswordService passwordService;
    private final UserMapper userMapper;

    @GetMapping
    public UserProfile currentUser() {
        return SecurityUtils.getCurrentUser()
                .map(userMapper::toUserProfile)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        passwordService.changePassword(request);
    }
}
