package com.postretail.backend.cashsession.domain.port.out;

import com.postretail.backend.cashsession.domain.model.CashMovement;

import java.math.BigDecimal;
import java.util.List;

/**
 * Puerto de salida para persistir y consultar movimientos de efectivo.
 */
public interface CashMovementRepository {

    CashMovement save(CashMovement movement);

    /**
     * Movimientos de una sesión, para listarlos o auditarlos.
     */
    List<CashMovement> findBySessionId(Long cashSessionId);

    /**
     * Suma de movimientos de un tipo (INCOME o EXPENSE) en una sesión.
     * La usa el corte para calcular el efectivo esperado.
     * Devuelve cero si no hay movimientos de ese tipo.
     */
    BigDecimal totalByType(Long cashSessionId, String type);
}
