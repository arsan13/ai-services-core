package com.arsan.chatbot.entity;

import com.arsan.chatbot.enums.AuthProviderType;
import com.arsan.chatbot.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_username", columnNames = "username")
        },
        indexes = {
                @Index(name = "idx_provider_id_provider_type", columnList = "providerId, providerType")
        }
)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(nullable = false)
    private String username;

    @ToString.Exclude
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private RoleType role = RoleType.ROLE_USER;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType = AuthProviderType.LOCAL;

    private String providerId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
