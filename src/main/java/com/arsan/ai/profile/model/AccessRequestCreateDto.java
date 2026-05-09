package com.arsan.ai.profile.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestCreateDto {

    private String requesterComment;

    @NotEmpty
    private Set<String> roles;

    @NotEmpty
    private Set<String> permissions;
}
