package com.arsan.ai.core.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "app.api.response.wrapper")
public class ResponseWrapperProperties {

    private boolean enabled = true;

    @NotBlank
    private String basePackage;

    @NotEmpty
    private List<String> excludedPaths = new ArrayList<>();
}
