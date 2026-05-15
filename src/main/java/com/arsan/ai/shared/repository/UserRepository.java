package com.arsan.ai.shared.repository;

import com.arsan.ai.auth.enums.AuthProviderType;
import com.arsan.ai.shared.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    <T> List<T> findAllBy(Class<T> type);

    <T> Optional<T> findById(Long id, Class<T> type);

    Optional<AppUser> findByEmailIgnoreCase(String email);

    Optional<AppUser> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);

    boolean existsByEmailIgnoreCase(String email);
}
