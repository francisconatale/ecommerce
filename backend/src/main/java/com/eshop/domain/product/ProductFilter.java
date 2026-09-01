package com.eshop.domain.product;

import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductFilter {
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String name;
    
    private UUID categoryId;
    
    @DecimalMin(value = "0.0", message = "El precio mínimo no puede ser negativo")
    private BigDecimal minPrice;
    
    @DecimalMin(value = "0.0", message = "El precio máximo no puede ser negativo")
    private BigDecimal maxPrice;
}
