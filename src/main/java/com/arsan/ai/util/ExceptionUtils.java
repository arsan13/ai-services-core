package com.arsan.ai.util;

import com.arsan.ai.exception.custom.ResourceNotFoundException;

public class ExceptionUtils {

    private ExceptionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static ResourceNotFoundException userNotFound() {
        return new ResourceNotFoundException("User not found");
    }
}
