package com.arsan.ai.notification.email.constants;

public final class EmailConstants {

    public static final String VERIFY_EMAIL_SUBJECT = "Verify your email";
    public static final String RESET_PASSWORD_SUBJECT = "Reset your password";

    public static final String VERIFY_EMAIL_PATH = "/verify-email";
    public static final String RESET_PASSWORD_PATH = "/reset-password";

    public static final String VERIFY_EMAIL_TEMPLATE = "verify-email";
    public static final String RESET_PASSWORD_TEMPLATE = "reset-password";

    private EmailConstants() {
        throw new IllegalStateException("Constants class");
    }
}