package com.postretail.backend.cashsession.infrastructure.web.dto;

import com.postretail.backend.cashsession.domain.model.CashMovement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashMovementResponse(
        Long id,
        Long cashSessionId,
        String type,
        String concept,
        BigDecimal amount,
        String responsible,
        LocalDateTime createdAt
) {
    public static CashMovementResponse from(CashMovement movement) {
        return new CashMovementResponse(
                movement.getId(),
                movement.getCashSessionId(),
                movement.getType().name(),
                movement.getConcept(),
                movement.getAmount(),
                movement.getResponsible(),
                movement.getCreatedAt()
        );
    }
}
