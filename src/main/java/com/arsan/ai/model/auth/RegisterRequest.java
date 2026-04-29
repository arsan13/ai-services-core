package com.arsan.ai.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RegisterRequest extends AuthRequest {

    @NotBlank(message = "Name cannot be blank")
    private String fullName;
}
