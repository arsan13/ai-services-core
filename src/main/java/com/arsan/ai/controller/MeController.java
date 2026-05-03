package com.arsan.ai.controller;

import com.arsan.ai.exception.custom.ResourceNotFoundException;
import com.arsan.ai.mapper.UserMapper;
import com.arsan.ai.model.auth.ChangePasswordRequest;
import com.arsan.ai.model.common.UserProfile;
import com.arsan.ai.service.AuthService;
import com.arsan.ai.util.SecurityUtils;
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
public class MeController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @GetMapping
    public UserProfile currentUser() {
        return SecurityUtils.getCurrentUser()
                .map(userMapper::toUserProfile)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);
        return "Password updated. Please login!";
    }
}
