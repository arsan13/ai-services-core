package com.arsan.ai.security.jwt;

import com.arsan.ai.entity.User;
import com.arsan.ai.enums.TokenPurpose;
import com.arsan.ai.properties.EmailVerificationProperties;
import com.arsan.ai.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.arsan.ai.constants.SecurityConstants.CLAIM_AUTHORITIES;
import static com.arsan.ai.constants.SecurityConstants.CLAIM_EMAIL;
import static com.arsan.ai.constants.SecurityConstants.CLAIM_PROVIDER;
import static com.arsan.ai.constants.SecurityConstants.CLAIM_USER_ID;
import static com.arsan.ai.constants.SecurityConstants.TOKEN_PURPOSE;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecurityProperties securityProperties;
    private final EmailVerificationProperties emailVerificationProperties;
    private final JwtKeyProvider keyProvider;
    private final JwtParser jwtParser;

    public String generateAccessToken(User user) {
        long expiration = TimeUnit.HOURS.toMillis(securityProperties.getJwt().getExpirationInHours());

        Map<String, Object> claims = Map.of(
                CLAIM_USER_ID, user.getId(),
                CLAIM_EMAIL, user.getEmail(),
                CLAIM_PROVIDER, user.getProviderType().name(),
                CLAIM_AUTHORITIES, user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList(),
                TOKEN_PURPOSE, TokenPurpose.ACCESS.name()
        );

        return buildToken(user.getEmail(), claims, expiration, keyProvider.accessKey());
    }

    public String generateVerificationToken(User user) {
        long expiration = TimeUnit.MINUTES.toMillis(emailVerificationProperties.getExpirationInMinutes());

        Map<String, Object> claims = Map.of(
                CLAIM_USER_ID, user.getId(),
                CLAIM_EMAIL, user.getEmail(),
                TOKEN_PURPOSE, TokenPurpose.EMAIL_VERIFICATION.name()
        );

        return buildToken(user.getEmail(), claims, expiration, keyProvider.verificationKey());
    }

    private String buildToken(String subject, Map<String, Object> claims, long expiration, SecretKey key) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String extractEmailFromAccessToken(String token) {
        return jwtParser.parseAccessToken(token).get(CLAIM_EMAIL, String.class);
    }

    public String extractEmailFromVerificationToken(String token) {
        return jwtParser.parseVerificationToken(token).get(CLAIM_EMAIL, String.class);
    }

    public boolean isAccessTokenValid(String token, User user) {
        Claims claims = jwtParser.parseAccessToken(token);
        return user.getEmail().equals(claims.getSubject()) && isExpired(claims);
    }

    public boolean hasPurpose(String token, TokenPurpose purpose) {
        Claims claims = jwtParser.parseVerificationToken(token);
        return TokenPurpose.EMAIL_VERIFICATION.name().equals(claims.get(purpose.name()));
    }

    public boolean isVerificationTokenExpired(String token) {
        Claims claims = jwtParser.parseVerificationToken(token);
        return isExpired(claims);
    }

    public boolean isExpired(Claims claims) {
        return !claims.getExpiration().before(new Date());
    }
}
