package com.postretail.backend.sale.infrastructure.persistence;

import com.postretail.backend.product.infrastructure.persistence.ProductJpaEntity;
import com.postretail.backend.product.infrastructure.persistence.ProductJpaRepository;
import com.postretail.backend.sale.domain.port.out.LoadProductNamesPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LoadProductNamesAdapter implements LoadProductNamesPort {

    private final ProductJpaRepository productJpaRepository;

    public LoadProductNamesAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Map<Long, String> loadNamesByIds(List<Long> productIds) {
        return productJpaRepository
                .findAllById(productIds)
                .stream()
                .collect(
                        Collectors.toMap(
                                ProductJpaEntity::getId,
                                ProductJpaEntity::getName
                        )
                );
    }
}
