package com.postretail.backend.product.infrastructure.web.dto;

import com.postretail.backend.product.domain.model.Product;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest (

        @NotBlank(message = "El SKU es obligatorio")
        @Size(max = 50, message = "El SKU no puede exceder 50 caracteres")
        String sku,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
        String name,

        @Size(max = 255, message = "La descripción no puede exceder 255 carcterees")
        String description,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "1", message = "El precio debe ser mayor a cero")
        BigDecimal price,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock debe ser mayor a cero")
        Integer stock,

        boolean active,

        Long categoryId
) {
    // Convierte el request en un objeto de dominio (sin id, lo asigna la BD)
    public Product toDomain() {
        return new Product(null, sku, name, description, price, stock, active, categoryId);
    }
}
