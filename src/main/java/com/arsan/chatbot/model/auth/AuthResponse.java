package com.arsan.chatbot.model.auth;

import com.arsan.chatbot.enums.AuthProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private AuthProviderType provider = AuthProviderType.LOCAL;

    public AuthResponse(String token) {
        this.token = token;
    }
}
