package com.arsan.ai.model.ai;

import com.arsan.ai.enums.PermissionType;

public record ChatTypeResponse(String code, String displayName, PermissionType permission) {
}
