package com.arsan.ai.core.properties;

import com.arsan.ai.chat.enums.AiModelProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "app.chat")
public class ChatProviderProperties {

    @NotNull
    private AiModelProvider provider = AiModelProvider.OPENAI;
}
