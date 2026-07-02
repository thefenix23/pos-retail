package com.postretail.backend.cashsession.infrastructure.persistence;

import com.postretail.backend.cashsession.domain.model.CashSession;
import com.postretail.backend.cashsession.domain.model.CashStatus;
import com.postretail.backend.cashsession.domain.port.out.CashSessionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de salida. Implementa el contrato del dominio (CashSessionRepository)
 * usando Spring Data por debajo.
 *
 * Lleva @Component para que Spring lo auto-registre e inyecte en el Config.
 * (El caso de uso, en cambio, se registra como @Bean con new.)
 */
@Component
public class CashSessionRepositoryAdapter implements CashSessionRepository {

    private final CashSessionJpaRepository jpaRepository;

    public CashSessionRepositoryAdapter(CashSessionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsOpenSession(Long branchId, Long registerId) {
        return jpaRepository.existsByBranchIdAndRegisterIdAndStatus(
                branchId, registerId, CashStatus.OPEN.name()
        );
    }

    @Override
    public Optional<CashSession> findOpenSession(Long branchId, Long registerId) {
        return jpaRepository
                .findByBranchIdAndRegisterIdAndStatus(
                        branchId, registerId, CashStatus.OPEN.name()
                )
                .map(CashSessionMapper::toDomain);
    }

    @Override
    public Optional<CashSession> findById(Long id) {
        return jpaRepository
                .findById(id)
                .map(CashSessionMapper::toDomain);
    }

    @Override
    public CashSession save(CashSession session) {
        CashSessionJpaEntity saved = jpaRepository.save(CashSessionMapper.toEntity(session));

        return CashSessionMapper.toDomain(saved);
    }

    @Override
    public CashSession update(CashSession session) {
        // La entity lleva id, asi que save() ejecuta en lugar de INSERT
        CashSessionJpaEntity updated = jpaRepository.save(CashSessionMapper.toEntity(session));

        return CashSessionMapper.toDomain(updated);
    }
}
