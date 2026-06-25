package com.postretail.backend.product.application.usecase;

import com.postretail.backend.product.domain.exception.DuplicateSkuException;
import com.postretail.backend.product.domain.model.Product;
import com.postretail.backend.product.domain.port.in.CreateProductUseCase;
import com.postretail.backend.product.domain.port.out.ProductRepository;

public class CreateProductService implements CreateProductUseCase {

    private final ProductRepository productRepository;

    public CreateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {
        // Regla de negocio: el SKU no puede repetirse
        if (productRepository.existsBySku(product.getSku())) {
            throw new DuplicateSkuException(product.getSku());
        }

        return productRepository.save(product);
    }
}
