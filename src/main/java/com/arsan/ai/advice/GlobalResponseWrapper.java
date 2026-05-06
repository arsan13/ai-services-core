package com.arsan.ai.advice;

import com.arsan.ai.model.common.ApiResponse;
import com.arsan.ai.properties.ResponseWrapperProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    private static final String SUCCESS_MESSAGE = "Success";

    private final ResponseWrapperProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (!properties.isEnabled()) {
            return false;
        }
        return returnType.getContainingClass().getPackageName().startsWith(properties.getBasePackage());
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (shouldSkip(request)) {
            return body;
        }
        if (body instanceof ApiResponse<?>) {
            return body;
        }
        if (body instanceof ResponseEntity<?> responseEntity) {
            return wrapResponseEntity(responseEntity);
        }
        if (body instanceof String) {
            return wrapString((String) body, response);
        }

        return buildSuccessResponse(body);
    }

    private boolean shouldSkip(ServerHttpRequest request) {
        String relativePath = getRelativePath(request);
        return properties.getExcludedPaths().stream().anyMatch(relativePath::startsWith);
    }

    private String getRelativePath(ServerHttpRequest request) {
        String fullPath = request.getURI().getPath();

        if (request instanceof ServletServerHttpRequest servletRequest) {
            String contextPath = servletRequest.getServletRequest().getContextPath();

            if (fullPath.startsWith(contextPath)) {
                return fullPath.substring(contextPath.length());
            }
        }

        return fullPath;
    }

    private <T> ResponseEntity<ApiResponse<T>> wrapResponseEntity(ResponseEntity<T> responseEntity) {
        ApiResponse<T> wrapped = buildSuccessResponse(responseEntity.getBody());

        return ResponseEntity
                .status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(wrapped);
    }

    private String wrapString(String body, ServerHttpResponse response) {
        ApiResponse<String> wrapped = buildSuccessResponse(body);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return convertToJson(wrapped);
    }

    private <T> ApiResponse<T> buildSuccessResponse(T body) {
        return ApiResponse.success(body, SUCCESS_MESSAGE);
    }

    private String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting response to JSON", e);
        }
    }
}
