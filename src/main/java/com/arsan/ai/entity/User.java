package com.arsan.ai.entity;

import com.arsan.ai.enums.AuthProviderType;
import com.arsan.ai.enums.PermissionType;
import com.arsan.ai.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = User.EMAIL_UNIQUE_KEY_NAME, columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_user_provider_id_provider_type", columnList = "providerId, providerType")
        }
)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    public static final String EMAIL_UNIQUE_KEY_NAME = "uk_user_email";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(nullable = false)
    private String email;

    @ToString.Exclude
    private String password;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles = Set.of(RoleType.ROLE_USER);

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> permissions = Set.of(PermissionType.USER_READ.getValue(), PermissionType.CHAT_GENERIC_USE.getValue());

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType = AuthProviderType.LOCAL;

    private String providerId;

    private boolean verified;
    private LocalDateTime verifiedDate;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    private LocalDateTime passwordResetDate;


    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.addAll(this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList());

        authorities.addAll(permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .toList()
        );

        return authorities;
    }
}
