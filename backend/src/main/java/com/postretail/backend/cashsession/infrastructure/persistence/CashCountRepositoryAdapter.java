package com.postretail.backend.cashsession.infrastructure.persistence;

import com.postretail.backend.cashsession.domain.model.CashCount;
import com.postretail.backend.cashsession.domain.port.out.CashCountRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CashCountRepositoryAdapter implements CashCountRepository {

    private final CashCountJpaRepository jpaRepository;

    public CashCountRepositoryAdapter(CashCountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CashCount save(CashCount count) {
        CashCountJpaEntity saved = jpaRepository.save(
                CashCountMapper.toEntity(count)
        );

        return CashCountMapper.toDomain(saved);
    }

    @Override
    public Optional<CashCount> findBySessionId(Long cashSessionId) {
        return jpaRepository
                .findByCashSessionId(cashSessionId)
                .map(CashCountMapper::toDomain);
    }
}
