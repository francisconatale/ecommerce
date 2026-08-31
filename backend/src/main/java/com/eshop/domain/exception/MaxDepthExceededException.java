package com.eshop.domain.exception;

public class MaxDepthExceededException extends BusinessException {
    public MaxDepthExceededException(int maxDepth) {
        super("Exceeds maximum allowed depth of " + maxDepth + " levels.", "MAX_DEPTH_EXCEEDED");
    }
}
