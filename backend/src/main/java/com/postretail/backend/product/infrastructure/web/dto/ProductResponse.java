package com.postretail.backend.product.infrastructure.web.dto;

import com.postretail.backend.product.domain.model.Product;

import java.math.BigDecimal;

public record ProductResponse (
        Long id,
        String sku,
        String name,
        BigDecimal price,
        int stock,
        Long categoryId
) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategoryId()
        );
    }
}