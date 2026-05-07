package com.arsan.ai.auth.model;

import com.arsan.ai.core.annotation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequest {

    private String currentPassword;

    @ValidPassword
    private String newPassword;
}
