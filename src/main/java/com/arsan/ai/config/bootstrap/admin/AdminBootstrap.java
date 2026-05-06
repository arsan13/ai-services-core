package com.arsan.ai.config.bootstrap.admin;

import com.arsan.ai.entity.AppUser;
import com.arsan.ai.enums.PermissionType;
import com.arsan.ai.enums.RoleType;
import com.arsan.ai.properties.AdminBootstrapProperties;
import com.arsan.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.EnumSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminBootstrapProperties properties;

    @Override
    public void run(String... args) {
        if (!properties.isEnabled()) {
            return;
        }

        if (userRepository.existsByEmail(properties.getEmail())) {
            return;
        }

        log.info("Creating bootstrap admin user: {}", properties.getEmail());

        AppUser admin = AppUser.builder()
                .fullName("System Admin")
                .email(properties.getEmail())
                .password(passwordEncoder.encode(properties.getPassword()))
                .roles(EnumSet.allOf(RoleType.class))
                .permissions(PermissionType.ALL_VALUES)
                .verified(true)
                .verifiedDate(LocalDateTime.now())
                .build();

        userRepository.save(admin);
    }
}
