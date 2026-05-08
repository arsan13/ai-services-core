package com.arsan.ai.shared.util;

import com.arsan.ai.core.exception.custom.ResourceNotFoundException;

public class ExceptionUtils {

    private ExceptionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static ResourceNotFoundException userNotFound() {
        return new ResourceNotFoundException("User not found");
    }
}
