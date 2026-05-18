package com.arsan.ai.core.config.cache;

import com.arsan.ai.shared.cache.AppUserCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@ConditionalOnProperty(name = "app.cache.type", havingValue = "caffeine")
public class CaffeineCacheConfig {

    @Bean
    public CacheManager cacheManager() {

        CaffeineCacheManager manager = new CaffeineCacheManager(
                AppUserCache.USER_BY_ID_CACHE,
                AppUserCache.USER_BY_EMAIL_CACHE
        );

        manager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .maximumSize(10_000)
                        .recordStats()
        );

        return manager;
    }
}
