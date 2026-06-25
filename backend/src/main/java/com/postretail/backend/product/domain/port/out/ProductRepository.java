package com.postretail.backend.product.domain.port.out;

import com.postretail.backend.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findByNameContainingOrSkuContaining(String query);
    Optional<Product> findBySku(String sku);

    // CRUD
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);
    void deleteById(Long id);
    boolean existsBySku(String sku);
    boolean existsBySkuAndIdNot(String sku, Long id);
}
