package com.postretail.backend.cashsession.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashSessionJpaRepository extends JpaRepository<CashSessionJpaEntity, Long> {
    /**
     * Consulta derivada: existe una sesión con ese estado para esa caja.
     * Spring genera el SQL a partir del nombre del método
     */
    boolean existsByBranchIdAndRegisterIdAndStatus(Long branchId, Long registerId, String status);

    /**
     * Consulta derivada: la sessión con ese estado para esa caja, si existe.
     */
    Optional<CashSessionJpaEntity> findByBranchIdAndRegisterIdAndStatus(Long branchId, Long registerId, String status);
}
