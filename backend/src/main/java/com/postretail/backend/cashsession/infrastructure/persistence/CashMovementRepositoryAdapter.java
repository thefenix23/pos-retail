package com.postretail.backend.cashsession.infrastructure.persistence;

import com.postretail.backend.cashsession.domain.model.CashMovement;
import com.postretail.backend.cashsession.domain.port.out.CashMovementRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CashMovementRepositoryAdapter implements CashMovementRepository {

    private final CashMovementJpaRepository jpaRepository;

    public CashMovementRepositoryAdapter(CashMovementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CashMovement save(CashMovement movement) {
        CashMovementJpaEntity saved = jpaRepository.save(
                CashMovementMapper.toEntity(movement)
        );

        return CashMovementMapper.toDomain(saved);

    }

    @Override
    public List<CashMovement> findBySessionId(Long cashSessionId) {
        return jpaRepository
                .findByCashSessionId(cashSessionId)
                .stream()
                .map(CashMovementMapper::toDomain)
                .toList();
    }

    @Override
    public BigDecimal totalByType(Long cashSessionId, String type) {
        return jpaRepository.sumByType(cashSessionId, type);
    }
}
