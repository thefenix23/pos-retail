package com.postretail.backend.cashsession.infrastructure.web.dto;

import com.postretail.backend.cashsession.domain.model.CashCount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashCountResponse(
        Long id,
        Long cashSessionId,
        BigDecimal expectedCash,
        BigDecimal countedCash,
        BigDecimal difference,
        String approvedBy,
        LocalDateTime createdAt
) {
    public static CashCountResponse from(CashCount count) {
        return new CashCountResponse(
                count.getId(),
                count.getCashSessionId(),
                count.getExpectedCash(),
                count.getCountedCash(),
                count.getDifference(),
                count.getApprovedBy(),
                count.getCreatedAt()
        );
    }
}
