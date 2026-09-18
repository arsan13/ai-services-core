package com.arsan.ai.core.config.chat;

import com.arsan.ai.core.properties.ChatProviderProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ChatClientBuilderConfig {

    @Bean
    public ChatModel chatModel(ChatModelResolver chatModelResolver, ChatProviderProperties chatProviderProperties) {
        return chatModelResolver.resolve(chatProviderProperties.getProvider());
    }

    @Bean
    @Scope("prototype")
    public ChatClient.Builder chatClientBuilder(ChatModel chatModel) {
        return ChatClient.builder(chatModel);
    }
}
