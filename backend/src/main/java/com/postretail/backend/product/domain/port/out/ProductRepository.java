package com.postretail.backend.product.domain.port.out;

import com.postretail.backend.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findByNameContainingOrSkuContaining(String query);
    Optional<Product> findBySku(String sku);
}
