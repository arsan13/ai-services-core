package com.arsan.ai.profile.model;

import com.arsan.ai.auth.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestCreateDto {

    private String requesterComment;
    private Set<RoleType> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();
}
