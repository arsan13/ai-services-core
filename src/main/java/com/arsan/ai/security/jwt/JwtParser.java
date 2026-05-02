package com.arsan.ai.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtParser {

    private final JwtKeyProvider keyProvider;

    public Claims parseAccessToken(String token) {
        return parse(token, keyProvider.accessKey());
    }

    public Claims parseVerificationToken(String token) {
        return parse(token, keyProvider.verificationKey());
    }

    private Claims parse(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
