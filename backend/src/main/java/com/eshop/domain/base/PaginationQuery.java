package com.eshop.domain.base;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class PaginationQuery {
    private int page;
    private int size;
}
