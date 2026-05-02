package com.arsan.ai.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.verification.email")
public class EmailVerificationProperties {

    private String secret;
    private long expirationInMinutes;
}
