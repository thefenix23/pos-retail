package com.postretail.backend.cashsession.infrastructure.persistence;

import com.postretail.backend.cashsession.domain.model.CashCount;
import com.postretail.backend.cashsession.domain.model.CashMovement;
import com.postretail.backend.cashsession.domain.model.MovementType;

final class CashMovementMapper {

    private CashMovementMapper() {}

    static CashMovementJpaEntity toEntity(CashMovement movement) {
        return new CashMovementJpaEntity(
                movement.getId(),
                movement.getCashSessionId(),
                movement.getType().name(),
                movement.getConcept(),
                movement.getAmount(),
                movement.getResponsible(),
                movement.getCreatedAt()
        );
    }

    static CashMovement toDomain(CashMovementJpaEntity entity) {
        return new CashMovement(
                entity.getId(),
                entity.getCashSessionId(),
                MovementType.valueOf(entity.getType()),
                entity.getConcept(),
                entity.getAmount(),
                entity.getResponsible(),
                entity.getCreatedAt()
        );
    }
}
