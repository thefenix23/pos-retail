package com.postretail.backend.product.application.usecase;

import com.postretail.backend.product.domain.exception.DuplicateSkuException;
import com.postretail.backend.product.domain.exception.ProductNotFoundException;
import com.postretail.backend.product.domain.model.Product;
import com.postretail.backend.product.domain.port.out.ProductRepository;

public class UpdateProductService implements com.postretail.backend.product.domain.port.in.UpdateProductUseCase {

    private final ProductRepository productRepository;

    public UpdateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product update(Long id, Product product) {
        // Debe existir
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }

        // El SKU no puede chocar con OTRO producto distinto
        if (productRepository.existsBySkuAndIdNot(product.getSku(), id)) {
            throw new DuplicateSkuException(product.getSku());
        }

        // Asegura que se guarde con el id correcto de la ruta
        return productRepository.save(product.winthId(id));
    }
}
