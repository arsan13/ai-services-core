package com.arsan.ai.security.jwt;

import com.arsan.ai.properties.EmailVerificationProperties;
import com.arsan.ai.properties.SecurityProperties;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtKeyProvider {

    /**
     * PRODUCTION USAGE:
     * Use a strong, cryptographically secure key stored as a Base64-encoded string.
     * Decode it before creating the signing key:
     * <p>
     * return Keys.hmacShaKeyFor(
     * Decoders.BASE64.decode(securityProperties.getJwt().getSecret())
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

    private final SecurityProperties securityProperties;
    private final EmailVerificationProperties emailVerificationProperties;

    public SecretKey accessKey() {
        return Keys.hmacShaKeyFor(
                securityProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    public SecretKey verificationKey() {
        return Keys.hmacShaKeyFor(
                emailVerificationProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }
}
