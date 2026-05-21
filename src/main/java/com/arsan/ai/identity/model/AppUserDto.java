package com.arsan.ai.identity.model;

import com.arsan.ai.auth.enums.AuthProviderType;
import com.arsan.ai.identity.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppUserDto implements UserDetails, Serializable {

    private Long id;
    private String fullName;
    private String email;
    private String password;

    private Set<RoleType> roles;
    private Set<String> permissions;

    private AuthProviderType providerType;

    private boolean verified;
    private LocalDateTime passwordResetDate;
    private Integer tokenVersion;

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream
                .concat(
                        this.roles.stream().map(role -> new SimpleGrantedAuthority(role.name())),
                        this.permissions.stream().map(SimpleGrantedAuthority::new)
                )
                .collect(Collectors.toUnmodifiableSet());
    }
}
