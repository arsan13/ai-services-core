package com.arsan.ai.model.auth;

import com.arsan.ai.annotation.ValidPassword;
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
public class AuthRequest extends EmailRequest {

    @ValidPassword
    private String password;
}
