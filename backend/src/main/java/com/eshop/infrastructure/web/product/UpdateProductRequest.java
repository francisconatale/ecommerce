package com.eshop.infrastructure.web.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductRequest(
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    String name,
    
    @DecimalMin(value = "0.0", message = "El precio de compra no puede ser negativo")
    BigDecimal priceBuy,
    
    @DecimalMin(value = "0.0", message = "El precio de venta no puede ser negativo")
    BigDecimal priceSell,
    
    UUID categoryId
) {}
