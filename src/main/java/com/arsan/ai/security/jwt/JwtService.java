package com.arsan.ai.security.jwt;

import com.arsan.ai.entity.User;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.arsan.ai.constants.SecurityConstants.CLAIM_AUTHORITIES;
import static com.arsan.ai.constants.SecurityConstants.CLAIM_EMAIL;
import static com.arsan.ai.constants.SecurityConstants.CLAIM_PROVIDER;
import static com.arsan.ai.constants.SecurityConstants.CLAIM_USER_ID;
import static com.arsan.ai.constants.SecurityConstants.TOKEN_PURPOSE;

@Service
public class JwtService {

    private final SecurityProperties.Jwt jwtProperties;
    private final Map<TokenPurpose, Long> tokenPurposeExpirationMap;

    public JwtService(SecurityProperties securityProperties) {
        this.jwtProperties = securityProperties.getJwt();
        tokenPurposeExpirationMap = Map.of(
                TokenPurpose.ACCESS, jwtProperties.getAccessExpirationInMinutes(),
                TokenPurpose.EMAIL_VERIFICATION, jwtProperties.getEmailVerificationExpirationInMinutes(),
                TokenPurpose.PASSWORD_RESET, jwtProperties.getPasswordResetExpirationInMinutes()
        );
    }

    public String generateToken(User user, TokenPurpose tokenPurpose) {
        Map<String, Object> claims = new HashMap<>(Map.of(
                CLAIM_USER_ID, user.getId(),
                CLAIM_EMAIL, user.getEmail(),
                TOKEN_PURPOSE, tokenPurpose.name()
        ));

        if (TokenPurpose.ACCESS.equals(tokenPurpose)) {
            claims.put(CLAIM_PROVIDER, user.getProviderType());
            claims.put(CLAIM_AUTHORITIES, user.getAuthorities());
        }

        return generateToken(claims, user, tokenPurposeExpirationMap.get(tokenPurpose));
    }

    private String generateToken(Map<String, Object> extraClaims, User user, long expirationInMinutes) {
        long expirationInMs = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expirationInMinutes);
        return Jwts.builder()
                .subject(user.getEmail())
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(expirationInMs))
                .signWith(getSignKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public boolean isTokenValid(String token, User user) {
        final String email = extractEmail(token);
        return email.equals(user.getEmail()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean hasInvalidPurpose(String token, TokenPurpose expectedPurpose) {
        String purpose = extractClaim(token, claims -> claims.get(TOKEN_PURPOSE, String.class));
        return !expectedPurpose.name().equals(purpose);
    }

    public void validateToken(String token, TokenPurpose expectedPurpose) {
        if (hasInvalidPurpose(token, expectedPurpose)) {
            throw new IllegalArgumentException("Invalid token purpose");
        }

        if (isTokenExpired(token)) {
            throw new IllegalArgumentException("Token expired");
        }
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
         *     Decoders.BASE64.decode(jwtProperties.getSecret())
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
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }
}
