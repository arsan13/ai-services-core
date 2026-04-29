package com.arsan.ai.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.api.response.wrapper")
public class ResponseWrapperProperties {

    private boolean enabled = true;
    private String basePackage;
    private List<String> excludedPaths = new ArrayList<>();
}
