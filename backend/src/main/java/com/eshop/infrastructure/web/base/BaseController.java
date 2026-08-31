package com.eshop.infrastructure.web.base;

import com.eshop.infrastructure.web.base.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.URI;

// responseEntity se encarga de serializar la lista o el objeto que le llegue
public abstract class BaseController {
    protected <T> ResponseEntity<ApiResponse<T>> created(T data, String path) {
        return ResponseEntity
                .created(URI.create(path))
                .body(ApiResponse.success(data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> emptyOk() {
        return ResponseEntity.ok(ApiResponse.empty());
    }

    protected <T> ResponseEntity<T> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
