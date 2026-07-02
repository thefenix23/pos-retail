package com.postretail.backend.cashsession.domain.port.out;

import java.math.BigDecimal;

/**
 * Puerto de salida propiedad del módulo cashsession.
 * Le permite saber cuánto se cobró en efectivo en las ventas de una sesión,
 * sin conocer las clases del módulo sale. Un adpatador en sale lo implementa.
 */
public interface CashSalesSummaryPort {
    /**
     * Total cobra en efectivo (método de pagp "Cash") en las ventas
     * de la sesión de caja indicada. Devuelve cero si no hubo ventas en efectivo.
     */
    BigDecimal cashTotalForSession(Long cashSessionId);
}
