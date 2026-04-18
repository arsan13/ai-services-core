package com.arsan.chatbot.service.impl;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.enums.Role;
import com.arsan.chatbot.model.auth.AuthRequest;
import com.arsan.chatbot.model.auth.AuthResponse;
import com.arsan.chatbot.model.auth.AvailabilityResponse;
import com.arsan.chatbot.model.auth.RegisterRequest;
import com.arsan.chatbot.repository.UserRepository;
import com.arsan.chatbot.security.JwtService;
import com.arsan.chatbot.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        user = userRepository.save(user);
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    @Override
    public AvailabilityResponse checkAvailability(String field, String value) {
        boolean available = switch (field.toLowerCase()) {
            case "username" -> !userRepository.existsByUsername(value);
            case "email" -> !userRepository.existsByEmail(value);
            default -> throw new IllegalArgumentException("Invalid field: " + field);
        };

        return new AvailabilityResponse(available);
    }
}
