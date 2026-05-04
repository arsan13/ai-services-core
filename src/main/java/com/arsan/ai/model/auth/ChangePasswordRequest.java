package com.arsan.ai.model.auth;

import com.arsan.ai.annotation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequest {

    private String currentPassword;

    @ValidPassword
    private String newPassword;
}
