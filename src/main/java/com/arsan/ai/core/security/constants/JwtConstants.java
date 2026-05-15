package com.arsan.ai.core.security.constants;

public final class JwtConstants {

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_PROVIDER = "provider";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String CLAIM_TOKEN_PURPOSE = "purpose";
    public static final String CLAIM_TOKEN_VERSION = "tokenVersion";

    private JwtConstants() {
        throw new IllegalStateException("Constants class");
    }
}
