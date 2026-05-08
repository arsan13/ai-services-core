package com.arsan.ai.notification.email.constants;

public class BrevoEmailKeyConstants {

    public static final String SENDER = "sender";
    public static final String NAME = "name";
    public static final String EMAIL = "email";

    public static final String TO = "to";
    public static final String CC = "cc";
    public static final String BCC = "bcc";
    public static final String SUBJECT = "subject";
    public static final String HTML_CONTENT = "htmlContent";
    public static final String API_KEY_HEADER = "api-key";

    private BrevoEmailKeyConstants() {
        throw new IllegalStateException("Constants class");
    }
}
