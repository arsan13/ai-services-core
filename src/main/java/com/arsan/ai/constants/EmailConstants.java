package com.arsan.ai.constants;

public final class EmailConstants {

    public static final String VERIFY_SUBJECT = "Verify your email";
    public static final String RESET_SUBJECT = "Reset your password";
    public static final String VERIFY_PATH = "/verify-email";
    public static final String RESET_PATH = "/reset-password";

    private EmailConstants() {
        throw new IllegalStateException("Constants class");
    }
}