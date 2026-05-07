package com.arsan.ai.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "app.email")
public class EmailProperties {

    @NotBlank
    private String provider;

    @Valid
    @NotNull
    private Brevo brevo;

    @Getter
    @Setter
    public static class Brevo {

        @NotBlank
        private String url;

        @NotBlank
        private String apiKey;

        @NotBlank
        @Email
        private String senderEmail;

        @NotBlank
        private String senderName;
    }
}
