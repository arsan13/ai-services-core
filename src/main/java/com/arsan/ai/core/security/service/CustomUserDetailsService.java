package com.arsan.ai.core.security.service;

import com.arsan.ai.identity.cache.AppUserCache;
import com.arsan.ai.identity.model.AppUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserCache userCache;

    @Override
    public AppUserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        return userCache.getByEmail(email);
    }
}
