package com.postretail.backend.cashsession.infrastructure.web.dto;

import com.postretail.backend.cashsession.domain.model.CashSession;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Representación de salida de una sesión de caja hacia el cliente.
 */
public record CashSessionResponse(
        Long id,
        Long branchId,
        Long registerId,
        String cashier,
        BigDecimal initialFund,
        String status,
        LocalDateTime openedAt,
        LocalDateTime closedAt
) {
    public static CashSessionResponse from(CashSession session) {
        return new CashSessionResponse(
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
}
