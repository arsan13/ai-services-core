package com.arsan.ai.profile.service.impl;

import com.arsan.ai.profile.model.ChangePasswordRequest;
import com.arsan.ai.profile.model.UserProfile;
import com.arsan.ai.profile.service.ProfileService;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.mapper.UserMapper;
import com.arsan.ai.shared.repository.UserRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import com.arsan.ai.shared.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfile getUserProfile() {
        return SecurityUtils.getCurrentUser()
                .map(userMapper::toUserProfile)
                .orElseThrow(ExceptionUtils::userNotFound);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        AppUser user = SecurityUtils.getCurrentUser().orElseThrow(ExceptionUtils::userNotFound);

        if (user.getPassword() == null) {
            throw new IllegalStateException("Password change not allowed for this account");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different");
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetDate(LocalDateTime.now());

        userRepository.save(user);
    }
}
