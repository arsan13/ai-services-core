package com.arsan.ai.constants;

public final class SecurityConstants {

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_PROVIDER = "provider";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String TOKEN_PURPOSE = "purpose";

    private SecurityConstants() {
        throw new IllegalStateException("Constants class");
    }
}
