package com.arsan.ai.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RegisterRequest extends EmailRequest {

    @NotBlank(message = "Name cannot be blank")
    private String fullName;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
