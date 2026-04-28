package com.arsan.chatbot.advice;

import com.arsan.chatbot.model.common.ApiResponse;
import com.arsan.chatbot.properties.ResponseWrapperProperties;
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

        return wrapBody(body);
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

    private ResponseEntity<?> wrapResponseEntity(ResponseEntity<?> responseEntity) {
        Object body = responseEntity.getBody();

        if (body instanceof ApiResponse<?>) {
            return responseEntity;
        }

        ApiResponse<Object> wrappedBody = buildSuccessResponse(body);

        return ResponseEntity
                .status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(wrappedBody);
    }

    private ApiResponse<Object> wrapBody(Object body) {
        return buildSuccessResponse(body);
    }

    private ApiResponse<Object> buildSuccessResponse(Object body) {
        return ApiResponse.success(body, SUCCESS_MESSAGE);
    }
}
