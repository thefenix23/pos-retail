package com.postretail.backend.sale.infrastructure.persistence;

import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.port.out.SaleRepository;
import org.springframework.stereotype.Component;

@Component
public class SaleRepositoryAdapter implements SaleRepository {

    private final SaleJpaRepository jpaRepository;

    public SaleRepositoryAdapter(SaleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Sale save(Sale sale) {
        SaleJpaEntity entity = SaleMapper.toJpaEntity(sale);
        SaleJpaEntity saved = jpaRepository.save(entity);
        return SaleMapper.toDomain(saved);
    }
}
