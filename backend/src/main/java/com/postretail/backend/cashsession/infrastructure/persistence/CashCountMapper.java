package com.postretail.backend.cashsession.infrastructure.persistence;

import com.postretail.backend.cashsession.domain.model.CashCount;

final class CashCountMapper {

    private CashCountMapper() {}

    static CashCountJpaEntity toEntity(CashCount count) {
        return new CashCountJpaEntity(
                count.getId(),
                count.getCashSessionId(),
                count.getExpectedCash(),
                count.getCountedCash(),
                count.getDifference(),
                count.getApprovedBy(),
                count.getCreatedAt()
        );
    }

    static CashCount toDomain(CashCountJpaEntity entity) {
        return new CashCount(
                entity.getId(),
                entity.getCashSessionId(),
                entity.getExpectedCash(),
                entity.getCountedCash(),
                entity.getDifference(),
                entity.getApprovedBy(),
                entity.getCreatedAt()
        );
    }
}
