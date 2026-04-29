package com.arsan.chatbot.model.auth;

import com.arsan.chatbot.model.common.UserProfile;
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
    private UserProfile userProfile;

    public AuthResponse(String token) {
        this.token = token;
    }
}
