package com.postretail.backend.sale.infrastructure.persistence;

import com.postretail.backend.product.domain.model.Product;
import com.postretail.backend.product.infrastructure.persistence.ProductJpaRepository;
import com.postretail.backend.product.infrastructure.persistence.ProductMapper;
import com.postretail.backend.sale.domain.port.out.LoadProductPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoadProductAdapter implements LoadProductPort {

    private final ProductJpaRepository productJpaRepository;

    public LoadProductAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Optional<Product> loadById(Long productId) {
        return productJpaRepository
                .findById(productId)
                .map(ProductMapper::toDomain);
    }
}
