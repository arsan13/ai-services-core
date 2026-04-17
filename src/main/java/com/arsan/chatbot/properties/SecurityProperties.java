package com.arsan.chatbot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private boolean enabled;
    private List<String> publicEndpoints = new ArrayList<>();
    private Jwt jwt = new Jwt();

    @Setter
    @Getter
    public static class Jwt {
        private String secret;
        private long expirationInHours;
        private long refreshExpirationInDays;
    }
}
