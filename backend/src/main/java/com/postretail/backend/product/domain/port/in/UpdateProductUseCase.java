package com.postretail.backend.product.domain.port.in;

import com.postretail.backend.product.domain.model.Product;

public interface UpdateProductUseCase {
    Product update(Long id, Product product);
}
