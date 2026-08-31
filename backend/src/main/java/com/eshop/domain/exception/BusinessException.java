package com.eshop.domain.exception;

public abstract class BusinessException extends RuntimeException {
    
    private final String code;

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
