package com.arsan.ai.notification.email.constants;

public final class EmailConstants {

    public static final String VERIFY_EMAIL_SUBJECT = "Verify your email";
    public static final String RESET_PASSWORD_SUBJECT = "Reset your password";
    public static final String ACCESS_REQUEST_APPROVED_SUBJECT = "Your Access Request Has Been Approved";
    public static final String ACCESS_REQUEST_REJECTED_SUBJECT = "Your Access Request Has Been Rejected";

    public static final String VERIFY_EMAIL_PATH = "/verify-email";
    public static final String RESET_PASSWORD_PATH = "/reset-password";
    public static final String ACCESS_REQUEST_CREATE_PATH = "/request-create";

    public static final String VERIFY_EMAIL_TEMPLATE = "verify-email";
    public static final String RESET_PASSWORD_TEMPLATE = "reset-password";
    public static final String ACCESS_REQUEST_APPROVED_TEMPLATE = "access-request-approved";
    public static final String ACCESS_REQUEST_REJECTED_TEMPLATE = "access-request-rejected";

    private EmailConstants() {
        throw new IllegalStateException("Constants class");
    }
}