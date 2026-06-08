package com.postretail.backend.product.application.usecase;

import com.postretail.backend.product.domain.model.Product;
import com.postretail.backend.product.domain.port.in.SearchProductsUseCase;
import com.postretail.backend.product.domain.port.out.ProductRepository;

import java.util.List;
import java.util.Optional;

public class SearchProductsService implements SearchProductsUseCase {

    private final ProductRepository productRepository;

    public SearchProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> searchByNameOrSku(String query) {
        if (query == null || query.isBlank() || query.length() < 2) {
            return List.of();
        }

        return productRepository
                .findByNameContainingOrSkuContaining(query.trim())
                .stream()
                .filter(Product::isAvailable)
                .toList();
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        if (sku == null || sku.isBlank()) {
            return Optional.empty();
        }

        return productRepository.findBySku(sku.trim());
    }
}
