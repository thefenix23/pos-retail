package com.postretail.backend.product.application.usecase;

import com.postretail.backend.product.domain.model.Product;
import com.postretail.backend.product.domain.port.in.ListProductsUseCase;
import com.postretail.backend.product.domain.port.out.ProductRepository;

import java.util.List;

public class ListProductsService implements ListProductsUseCase {

    private final ProductRepository productRepository;

    public ListProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
