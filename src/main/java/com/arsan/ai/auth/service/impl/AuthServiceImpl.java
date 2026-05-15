package com.arsan.ai.auth.service.impl;

import com.arsan.ai.auth.enums.TokenPurpose;
import com.arsan.ai.auth.events.EmailVerificationRequestedEvent;
import com.arsan.ai.auth.model.AuthRequest;
import com.arsan.ai.auth.model.AuthResponse;
import com.arsan.ai.auth.model.AvailabilityResponse;
import com.arsan.ai.auth.model.RegisterRequest;
import com.arsan.ai.auth.service.AuthService;
import com.arsan.ai.core.security.service.JwtService;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.mapper.UserMapper;
import com.arsan.ai.shared.repository.UserRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.arsan.ai.auth.enums.PermissionType.CHAT_GENERIC_USE;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AppUser user = (AppUser) authentication.getPrincipal();
        String token = jwtService.generateToken(user, TokenPurpose.ACCESS);
        return new AuthResponse(token, userMapper.toUserProfile(user));
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        AppUser user = AppUser.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        sendVerificationEmail(user);
        String token = jwtService.generateToken(user, TokenPurpose.ACCESS);
        return new AuthResponse(token, userMapper.toUserProfile(user));
    }

    @Override
    public AvailabilityResponse isEmailAvailable(String email) {
        return new AvailabilityResponse(!userRepository.existsByEmailIgnoreCase(email));
    }

    @Override
    public void resendVerificationEmail(String email) {
        AppUser user = userRepository.findByEmailIgnoreCase(email).orElseThrow(ExceptionUtils::userNotFound);

        if (user.isVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        sendVerificationEmail(user);
    }

    @Override
    @Transactional
    public void verifyUser(String token) {
        jwtService.validateToken(token, TokenPurpose.EMAIL_VERIFICATION);

        String email = jwtService.extractEmail(token);
        AppUser user = userRepository.findByEmailIgnoreCase(email).orElseThrow(ExceptionUtils::userNotFound);

        markUserAsVerified(user);
    }

    @Override
    public void markUserAsVerified(AppUser user) {
        if (user.isVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        user.getExtraPermissions().add(CHAT_GENERIC_USE.getValue());
        user.setVerified(true);
        user.setVerifiedDate(LocalDateTime.now());
    }

    private void sendVerificationEmail(AppUser user) {
        eventPublisher.publishEvent(new EmailVerificationRequestedEvent(user));
    }
}
