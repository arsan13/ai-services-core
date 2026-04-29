package com.arsan.ai.model.common;

import com.arsan.ai.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleActionRequest {

    @NotNull(message = "Role type is required")
    private RoleType role;
}
