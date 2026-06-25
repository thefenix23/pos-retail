package com.postretail.backend.product.application.usecase;

import com.postretail.backend.product.domain.exception.ProductNotFoundException;
import com.postretail.backend.product.domain.port.in.DeleteProductUseCase;
import com.postretail.backend.product.domain.port.out.ProductRepository;

public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepository productRepository;

    public DeleteProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void delete(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
    }
}
