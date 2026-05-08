package com.arsan.ai.auth.model;

import com.arsan.ai.profile.model.UserProfile;
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
