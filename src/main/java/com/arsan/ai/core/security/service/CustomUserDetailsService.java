package com.arsan.ai.core.security.service;

import com.arsan.ai.identity.cache.AppUserCache;
import com.arsan.ai.identity.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserCache userCache;

    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return userCache.getByEmail(email);
    }
}
