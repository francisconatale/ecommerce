package com.eshop.domain.exception;

public class ResourceDuplicatedException extends BusinessException {

    public ResourceDuplicatedException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue), "RESOURCE_DUPLICATED");
    }

    public ResourceDuplicatedException(String message) {
        super(message, "RESOURCE_DUPLICATED");
    }
}
