package com.postretail.backend.cashsession.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CashMovementJpaRepository extends JpaRepository<CashMovementJpaEntity, Long> {

    List<CashMovementJpaEntity> findByCashSessionId(Long cashSessionId);

    /**
     * Suma de montos de un tipo en una sesión.
     * COALESCE devuelve 0 si no hay movimientos de ese tipo (evita null).
     */
    @Query("""
            SELECT COALESCE(SUM(m.amount), 0)
            FROM CashMovementJpaEntity m
            WHERE m.cashSessionId = :sessionId AND m.type = :type
            """)
    BigDecimal sumByType(@Param("sessionId") Long sessionId, @Param("type") String type);
}
