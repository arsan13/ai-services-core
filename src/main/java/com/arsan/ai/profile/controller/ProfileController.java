package com.arsan.ai.profile.controller;

import com.arsan.ai.profile.model.ChangePasswordRequest;
import com.arsan.ai.profile.model.UserProfile;
import com.arsan.ai.profile.service.ProfileService;
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

    private final ProfileService service;

    @GetMapping
    public UserProfile currentUser() {
        return service.getUserProfile();
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        service.changePassword(request);
    }
}
