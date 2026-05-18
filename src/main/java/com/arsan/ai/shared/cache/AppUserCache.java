package com.arsan.ai.shared.cache;

import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.repository.UserRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserCache {

    public static final String USER_BY_ID_CACHE = "usersById";
    public static final String USER_BY_EMAIL_CACHE = "usersByEmail";

    private final UserRepository userRepository;

    @Cacheable(value = USER_BY_ID_CACHE, key = "#id")
    public AppUser getById(Long id) {
        return userRepository.findById(id).orElseThrow(ExceptionUtils::userNotFound);
    }

    @Cacheable(value = USER_BY_EMAIL_CACHE, key = "#email")
    public AppUser getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(ExceptionUtils::userNotFound);
    }

    @Caching(evict = {
            @CacheEvict(value = USER_BY_ID_CACHE, key = "#user.id"),
            @CacheEvict(value = USER_BY_EMAIL_CACHE, key = "#user.email")
    })
    public void evict(AppUser user) {
        // Intentionally empty: cache eviction is handled entirely by Spring AOP via @CacheEvict annotations.
        // This method exists only as a declarative hook to trigger eviction for both cache keys.
    }
}
