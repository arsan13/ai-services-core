package com.arsan.ai.shared.enums;

public enum AccessRequestStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED;

    public static AccessRequestStatus fromValue(String value) {
        try {
            return AccessRequestStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid access request status: " + value);
        }
    }
}
