package com.postretail.backend.sale.domain.port.out;

import com.postretail.backend.product.domain.model.Product;

import java.util.Optional;

public interface LoadProductPort {
    Optional<Product> loadById(Long productId);
}
