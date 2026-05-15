package com.arsan.ai.admin.model;

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
public class PermissionRequest {

    @NotEmpty(message = "At least one permission is required")
    private Set<String> permissions;
}
