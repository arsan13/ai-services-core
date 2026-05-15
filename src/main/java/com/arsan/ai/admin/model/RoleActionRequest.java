package com.arsan.ai.admin.model;

import com.arsan.ai.auth.enums.RoleType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleActionRequest {

    @NotEmpty
    private Set<RoleType> roles;
}
