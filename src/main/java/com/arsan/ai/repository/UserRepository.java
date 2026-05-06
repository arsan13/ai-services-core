package com.arsan.ai.repository;

import com.arsan.ai.entity.AppUser;
import com.arsan.ai.enums.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    <T> List<T> findAllBy(Class<T> type);

    <T> Optional<T> findById(Long id, Class<T> type);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);

    boolean existsByEmail(String email);
}
