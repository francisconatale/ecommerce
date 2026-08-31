package com.eshop.infrastructure.web.base;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    String message,
    T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(null, data);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> empty() {
        return new ApiResponse<>(null, null);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }
}