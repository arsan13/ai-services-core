package com.arsan.ai.shared.entity;

import com.arsan.ai.auth.enums.AuthProviderType;
import com.arsan.ai.auth.enums.RoleType;
import com.arsan.ai.shared.util.PermissionUtils;
import jakarta.persistence.CollectionTable;
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
import jakarta.persistence.JoinColumn;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = AppUser.EMAIL_UNIQUE_KEY_NAME, columnNames = "email")
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AppUser implements UserDetails {

    public static final String EMAIL_UNIQUE_KEY_NAME = "uk_user_email";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String fullName;

    @Column(nullable = false)
    private String email;

    @ToString.Exclude
    private String password;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles = new HashSet<>(Set.of(RoleType.ROLE_USER));

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "app_user_extra_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<String> extraPermissions = new HashSet<>();

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "app_user_revoked_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<String> revokedPermissions = new HashSet<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType = AuthProviderType.LOCAL;

    private String providerId;

    private boolean verified;

    private LocalDateTime verifiedDate;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    private LocalDateTime passwordResetDate;

    @Builder.Default
    private Integer tokenVersion = 0;

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream
                .concat(
                        this.roles.stream().map(role -> new SimpleGrantedAuthority(role.name())),
                        PermissionUtils.resolvePermissions(this).stream().map(SimpleGrantedAuthority::new)
                )
                .collect(Collectors.toUnmodifiableSet());
    }
}
