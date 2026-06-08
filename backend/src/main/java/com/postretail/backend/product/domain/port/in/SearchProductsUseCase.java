package com.postretail.backend.product.domain.port.in;

import com.postretail.backend.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface SearchProductsUseCase {

    List<Product> searchByNameOrSku(String query);
    Optional<Product> findBySku(String sku);
}
