package com.postretail.backend.cashsession.infrastructure.web.dto;

import java.math.BigDecimal;

/**
 * Datos que llegan en el POST de apertura
 * Es un DTO de transporte: vive en la frontera web, no entra al dominio.
 */
public record OpenCashSessionRequest (
        Long branchId,
        Long registerId,
        String cashier,
        BigDecimal initialFund
) {}
