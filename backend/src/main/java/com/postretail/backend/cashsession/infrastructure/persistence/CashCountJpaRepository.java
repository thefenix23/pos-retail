package com.postretail.backend.cashsession.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashCountJpaRepository extends JpaRepository<CashCountJpaEntity, Long> {
    Optional<CashCountJpaEntity> findByCashSessionId(Long cashSessionId);
}
