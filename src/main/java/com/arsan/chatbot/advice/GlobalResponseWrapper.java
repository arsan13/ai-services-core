package com.arsan.chatbot.advice;

import com.arsan.chatbot.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        String packageName = returnType.getContainingClass().getPackageName();
        return !packageName.contains("actuator") &&
                !packageName.contains("swagger");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body instanceof ApiResponse) {
            return body;
        }

        // If controller returns ResponseEntity
        if (body instanceof ResponseEntity<?> responseEntity) {
            Object responseBody = responseEntity.getBody();

            if (responseBody instanceof ApiResponse) {
                return responseEntity;
            }

            ApiResponse<Object> wrapped = ApiResponse.success(responseBody, "Success");
            return ResponseEntity
                    .status(responseEntity.getStatusCode())
                    .headers(responseEntity.getHeaders())
                    .body(wrapped);
        }

        // Handle null
        if (body == null) {
            return ApiResponse.success(null, "Success");
        }

        // Handle String separately
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(
                        ApiResponse.success(body, "Success")
                );
            } catch (Exception e) {
                throw new RuntimeException("Error wrapping response");
            }
        }

        // Default wrapping
        return ApiResponse.success(body, "Success");
    }
}
