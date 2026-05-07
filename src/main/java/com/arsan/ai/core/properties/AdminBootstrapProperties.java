package com.arsan.ai.core.properties;

import com.arsan.ai.core.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "app.bootstrap.admin")
public class AdminBootstrapProperties {

    private boolean enabled = true;

    @NotBlank
    private String email;

    @ValidPassword
    private String password;
}
