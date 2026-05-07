package com.arsan.ai.auth.model;

import com.arsan.ai.core.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest extends EmailAuthRequest {

    @ValidPassword
    private String password;
}
