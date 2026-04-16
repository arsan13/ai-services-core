package com.arsan.chatbot.security;

import com.arsan.chatbot.properties.SecurityProperties;
import com.arsan.chatbot.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecurityProperties securityProperties;

    public String generateToken(User user) {
        long expiration = TimeUnit.HOURS.toMillis(securityProperties.getJwt().getExpirationInHours());
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(securityProperties.getJwt().getSecret()));
    }
}
