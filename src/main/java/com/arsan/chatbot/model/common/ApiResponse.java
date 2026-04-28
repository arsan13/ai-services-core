package com.arsan.chatbot.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Object error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data, null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> failure(String message, Object error) {
        return new ApiResponse<>(false, message, null, error);
    }
}
