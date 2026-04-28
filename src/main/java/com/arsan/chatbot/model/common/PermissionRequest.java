package com.arsan.chatbot.model.common;

import com.arsan.chatbot.enums.PermissionType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequest {

    @NotEmpty(message = "At least one permission is required")
    private List<PermissionType> permissions;
}
