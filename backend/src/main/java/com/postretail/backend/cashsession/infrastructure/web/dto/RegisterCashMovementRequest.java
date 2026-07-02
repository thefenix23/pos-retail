package com.postretail.backend.cashsession.infrastructure.web.dto;

import com.postretail.backend.cashsession.domain.model.MovementType;

import java.math.BigDecimal;

public record RegisterCashMovementRequest(
        Long cashSessionId,
        MovementType type,
        String concept,
        BigDecimal amount,
        String responsible
) {
}
