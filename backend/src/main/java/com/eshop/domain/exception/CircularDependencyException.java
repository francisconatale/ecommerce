package com.eshop.domain.exception;

public class CircularDependencyException extends BusinessException {
    public CircularDependencyException(String message) {
        super(message, "CIRCULAR_DEPENDENCY_DETECTED");
    }
}
