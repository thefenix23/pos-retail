package com.postretail.backend.cashsession.domain.port.in;

import com.postretail.backend.cashsession.domain.model.CashSession;

import java.math.BigDecimal;

/**
 * Puerto de entrada: lo que el mundo exterior puede pedirle el dominio.
 * El controller depende de esta interfaz, nunca de la implementación
 */
public interface OpenCashSessionUseCase {
    CashSession open(Long branchId, Long registerId, String cashier, BigDecimal initialFund);
}
