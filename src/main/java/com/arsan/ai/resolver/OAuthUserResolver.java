package com.arsan.ai.resolver;

import com.arsan.ai.entity.User;
import com.arsan.ai.provider.core.OAuthUserInfo;
import com.arsan.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserResolver {

    private final UserRepository userRepository;

    public User resolve(OAuthUserInfo info) {

        User providerUser = userRepository.findByProviderIdAndProviderType(info.getProviderId(), info.getProviderType()).orElse(null);
        User emailUser = findByEmail(info.getEmail());

        // 1. New user
        if (providerUser == null && emailUser == null) {
            return createUser(info);
        }

        // 2. Login via provider
        if (providerUser != null) {
            return providerUser;
        }

        // 3. Link account
        if (emailUser.getProviderId() == null) {
            return link(emailUser, info);
        }

        // 4. Conflict
        throw new BadCredentialsException("This email is already registered with provider " + emailUser.getProviderType().name());
    }

    private User findByEmail(String email) {
        if (email == null || email.isBlank()) return null;
        return userRepository.findByUsername(email).orElse(null);
    }

    private User createUser(OAuthUserInfo info) {
        User user = new User();

        String username = info.getEmail() != null ? info.getEmail() : info.getName();
        if (userRepository.existsByUsername(username)) {
            username = info.getProviderType().name().toLowerCase() + "_" + info.getProviderId();
        }

        user.setUsername(username);
        user.setFullName(info.getName());
        user.setProviderType(info.getProviderType());
        user.setProviderId(info.getProviderId());

        return userRepository.save(user);
    }

    private User link(User user, OAuthUserInfo info) {
        user.setProviderId(info.getProviderId());
        user.setProviderType(info.getProviderType());
        return userRepository.save(user);
    }
}
