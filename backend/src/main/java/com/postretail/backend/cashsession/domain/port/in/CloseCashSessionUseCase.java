package com.postretail.backend.cashsession.domain.port.in;

import com.postretail.backend.cashsession.domain.model.CashCount;

import java.math.BigDecimal;

/**
 * Puerto de entrada: cerrar una caja abierta y generar su corte (arqueo)
 */
public interface CloseCashSessionUseCase {

    CashCount close(CloseCashSessionCommand command);

    record CloseCashSessionCommand(
            Long cashSessionId,
            BigDecimal countedCash,
            String approvedBy
    ) {}
}
