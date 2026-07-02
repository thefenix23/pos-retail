package com.postretail.backend.cashsession.infrastructure.persistence;

import com.postretail.backend.cashsession.domain.model.CashSession;
import com.postretail.backend.cashsession.domain.model.CashStatus;

/**
 * Traduce entre el dominio puro y la entity JPA.
 * Aísla al dominio de cualquier detalle de persistencia.
 */
final class CashSessionMapper {

    private CashSessionMapper() {}

    static CashSessionJpaEntity toEntity(CashSession session) {
        return new CashSessionJpaEntity(
                session.getId(),
                session.getBranchId(),
                session.getRegisterId(),
                session.getCashier(),
                session.getInitialFund(),
                session.getStatus().name(),
                session.getOpenedAt(),
                session.getClosedAt()
        );
    }

    static CashSession toDomain(CashSessionJpaEntity entity) {
        return new CashSession(
                entity.getId(),
                entity.getBranchId(),
                entity.getRegisterId(),
                entity.getCashier(),
                entity.getInitialFund(),
                CashStatus.valueOf(entity.getStatus()),
                entity.getOpenedAt(),
                entity.getClosedAt()
        );
    }
}
