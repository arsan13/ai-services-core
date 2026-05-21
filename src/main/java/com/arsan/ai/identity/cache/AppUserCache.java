package com.arsan.ai.identity.cache;

import com.arsan.ai.identity.mapper.UserMapper;
import com.arsan.ai.identity.model.AppUserDto;
import com.arsan.ai.identity.model.UserEvictDto;
import com.arsan.ai.identity.repository.UserRepository;
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
    private final UserMapper mapper;

    @Cacheable(value = USER_BY_ID_CACHE, key = "#id", sync = true)
    public AppUserDto getById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(ExceptionUtils::userNotFound);
    }

    @Cacheable(value = USER_BY_EMAIL_CACHE, key = "#email", sync = true)
    public AppUserDto getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(mapper::toDto)
                .orElseThrow(ExceptionUtils::userNotFound);
    }

    @Caching(evict = {
            @CacheEvict(value = USER_BY_ID_CACHE, key = "#dto.userId"),
            @CacheEvict(value = USER_BY_EMAIL_CACHE, key = "#dto.email")
    })
    public void evict(UserEvictDto dto) {
        // Intentionally empty: handled by Spring cache AOP.
    }
}
