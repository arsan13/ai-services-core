package com.arsan.ai.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.Locale;

@Getter
public class EmailAuthRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    private String email;

    public void setEmail(String email) {
        if (email != null) {
            this.email = email.trim().toLowerCase(Locale.ROOT);
        }
    }
}
