package com.postretail.backend.product.infrastructure.persistence;

import com.postretail.backend.product.domain.model.Product;

public class ProductMapper {

    private ProductMapper() {}

    public static Product toDomain(ProductJpaEntity entity) {
        return new Product(
                entity.getId(),
                entity.getSku(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.isActive(),
                entity.getCategoryId()
        );
    }

    public static ProductJpaEntity toJpaEntity(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity();

        entity.setId(product.getId());
        entity.setSku(product.getSku());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        entity.setActive(product.isActive());
        entity.setCategoryId(product.getCategoryId());

        return entity;
    }
}
