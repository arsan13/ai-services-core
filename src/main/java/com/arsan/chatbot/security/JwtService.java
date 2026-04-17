package com.arsan.chatbot.security;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecurityProperties securityProperties;

    public String generateToken(User user) {
        Map<String, Object> claims = Map.of(
                "userId", user.getId(),
                "roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
        return generateToken(claims, user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        long expiration = TimeUnit.HOURS.toMillis(securityProperties.getJwt().getExpirationInHours());
        return Jwts.builder()
                .subject(user.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {

        /**
         * PRODUCTION USAGE:
         * Use a strong, cryptographically secure key stored as a Base64-encoded string.
         * Decode it before creating the signing key:
         * <p>
         * return Keys.hmacShaKeyFor(
         *     Decoders.BASE64.decode(securityProperties.getJwt().getSecret())
         * );
         * <p>
         * To generate a secure key (run once and store safely in config):
         * <p>
         * SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
         * String base64Key = Encoders.BASE64.encode(key.getEncoded());
         * System.out.println(base64Key);
         * <p>
         * Store the generated value in application.properties or a secure secrets manager.
         */

        // DEVELOPMENT ONLY: uses plain text secret (not secure for production)
        return Keys.hmacShaKeyFor(securityProperties.getJwt().getSecret().getBytes());
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = Map.of(
                "userId", user.getId(),
                "type", "refresh"
        );
        long expiration = TimeUnit.DAYS.toMillis(securityProperties.getJwt().getRefreshExpirationInDays());
        return Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }
}
