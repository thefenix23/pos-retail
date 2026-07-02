package com.postretail.backend.cashsession.infrastructure.web.dto;

import java.math.BigDecimal;

public record CloseCashSessionRequest(
        BigDecimal countedCash,
        String approvedBy
) {
}
