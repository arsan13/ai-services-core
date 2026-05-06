package com.arsan.ai.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Validated
@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private boolean enabled = true;

    @NotEmpty
    private List<String> publicEndpoints = new ArrayList<>();

    @Valid
    @NotNull
    private Jwt jwt;

    @Setter
    @Getter
    public static class Jwt {

        @NotBlank
        private String secret;

        @Min(1)
        private long accessExpirationInMinutes;

        @Min(1)
        private long emailVerificationExpirationInMinutes;

        @Min(1)
        private long passwordResetExpirationInMinutes;
    }
}
