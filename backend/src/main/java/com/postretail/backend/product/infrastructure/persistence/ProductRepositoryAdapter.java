package com.postretail.backend.product.infrastructure.persistence;

import com.postretail.backend.product.domain.model.Product;
import com.postretail.backend.product.domain.port.out.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    public ProductRepositoryAdapter(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Product> findByNameContainingOrSkuContaining(String query) {
        return jpaRepository
                .searchByNameOrSku(query)
                .stream()
                .map(ProductMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return jpaRepository
                .findBySkuAndActiveTrue(sku)
                .map(ProductMapper::toDomain);
    }
}
