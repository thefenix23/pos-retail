package com.postretail.backend.sale.infrastructure.persistence;

import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.port.out.SaleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Sale> findAll() {
        return jpaRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(SaleMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Sale> findById(Long id) {
        return jpaRepository
                .findWithItemsById(id)
                .map(SaleMapper::toDomain);
    }
}
