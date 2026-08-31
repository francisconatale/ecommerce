package com.eshop.domain.exception;

public class SystemCategoryImmutableException extends BusinessException {
    public SystemCategoryImmutableException(String message) {
        super(message, "SYSTEM_CATEGORY_IMMUTABLE");
    }
}
