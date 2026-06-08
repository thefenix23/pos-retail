package com.postretail.backend.sale.infrastructure.persistence;

import com.postretail.backend.product.infrastructure.persistence.ProductJpaEntity;
import com.postretail.backend.product.infrastructure.persistence.ProductJpaRepository;
import com.postretail.backend.sale.domain.port.out.UpdateStockPort;
import org.springframework.stereotype.Component;

@Component
public class UpdateStockAdapter implements UpdateStockPort {

    private final ProductJpaRepository productJpaRepository;

    public UpdateStockAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public void decreaseStock(Long productId, int quantity) {
        ProductJpaEntity product = productJpaRepository
                .findById(productId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Product not found: " + productId
                        ));
        product.setStock(product.getStock() - quantity);
        productJpaRepository.save(product);
    }
}
