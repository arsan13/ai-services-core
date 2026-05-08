package com.arsan.ai.auth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {

    @NotBlank(message = "Token must not be blank")
    private String token;
}
