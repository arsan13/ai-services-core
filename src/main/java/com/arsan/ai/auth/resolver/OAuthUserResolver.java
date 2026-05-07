package com.arsan.ai.auth.resolver;

import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.auth.provider.core.OAuthUserInfo;
import com.arsan.ai.shared.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserResolver {

    private final UserRepository userRepository;

    public AppUser resolve(OAuthUserInfo info) {

        AppUser providerUser = userRepository.findByProviderIdAndProviderType(info.getProviderId(), info.getProviderType()).orElse(null);
        AppUser emailUser = findByEmail(info.getEmail());

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

    private AppUser findByEmail(String email) {
        if (email == null || email.isBlank()) return null;
        return userRepository.findByEmail(email).orElse(null);
    }

    private AppUser createUser(OAuthUserInfo info) {
        AppUser user = new AppUser();

        String email = info.getEmail() != null ? info.getEmail() : info.getName();
        if (userRepository.existsByEmail(email)) {
            email = info.getProviderType().name().toLowerCase() + "_" + info.getProviderId();
        }

        user.setEmail(email);
        user.setFullName(info.getName());
        user.setProviderType(info.getProviderType());
        user.setProviderId(info.getProviderId());
        user.markAsVerified();

        return userRepository.save(user);
    }

    private AppUser link(AppUser user, OAuthUserInfo info) {
        user.setProviderId(info.getProviderId());
        user.setProviderType(info.getProviderType());
        return userRepository.save(user);
    }
}
