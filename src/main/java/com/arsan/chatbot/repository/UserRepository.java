package com.arsan.chatbot.repository;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.enums.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    <T> List<T> findAllBy(Class<T> type);

    <T> Optional<T> findById(Long id, Class<T> type);

    Optional<User> findByUsername(String username);

    Optional<User> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);

    boolean existsByUsername(String username);
}
