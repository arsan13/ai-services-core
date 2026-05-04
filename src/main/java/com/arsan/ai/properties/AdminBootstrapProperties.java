package com.arsan.ai.properties;

import com.arsan.ai.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.bootstrap.admin")
@Validated
public class AdminBootstrapProperties {

    private boolean enabled;

    @NotBlank
    private String email;

    @ValidPassword
    private String password;
}
