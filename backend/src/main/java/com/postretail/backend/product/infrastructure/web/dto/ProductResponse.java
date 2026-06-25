package com.postretail.backend.product.infrastructure.web.dto;

import com.postretail.backend.product.domain.model.Product;

import java.math.BigDecimal;

public record ProductResponse (
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        int stock,
        boolean active,
        Long categoryId
) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.isActive(),
                product.getCategoryId()
        );
    }
}