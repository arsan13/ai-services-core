package com.arsan.ai.constants;

public final class EmailConstants {

    public static final String VERIFY_SUBJECT = "Verify your email";
    public static final String RESET_SUBJECT = "Reset your password";

    public static final String VERIFY_PATH = "/verify-email";
    public static final String RESET_PATH = "/reset-password";

    public static final String EMAIL_VERIFY_TEMPLATE = """
                Hi %s,
                
                Please click the link below to verify your email address:
                
                %s
                
                This link will expire in %d minutes.
                
                If you did not create an account, please ignore this email.
                
                Best regards,
                The AI Services Core Team
                """;

    public static final String EMAIL_RESET_TEMPLATE = """
                Hi %s,
                
                Please click the link below to reset your password:
                
                %s
                
                This link will expire in %d minutes.
                
                If you did not request a password reset, please ignore this email.
                
                Best regards,
                The AI Services Core Team
                """;

    private EmailConstants() {
        throw new IllegalStateException("Constants class");
    }
}