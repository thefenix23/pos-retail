package com.postretail.backend.sale.infrastructure.web.dto;

import java.math.BigDecimal;

public record PaymentRequest(
        Long paymentMethodId,
        BigDecimal amount
) {}
