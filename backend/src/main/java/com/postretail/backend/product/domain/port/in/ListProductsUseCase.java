package com.postretail.backend.product.domain.port.in;

import com.postretail.backend.product.domain.model.Product;

import java.util.List;

public interface ListProductsUseCase {
    List<Product> findAll();
}
