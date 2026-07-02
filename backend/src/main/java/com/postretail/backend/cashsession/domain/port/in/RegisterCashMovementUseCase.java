package com.postretail.backend.cashsession.domain.port.in;

import com.postretail.backend.cashsession.domain.model.CashMovement;
import com.postretail.backend.cashsession.domain.model.MovementType;

import java.math.BigDecimal;

/**
 * Puerto de entrada: registrar un ingreso o egreso de efectivo en una caja abierta.
 */
public interface RegisterCashMovementUseCase {

    CashMovement register(RegisterCashMovementCommand command);

    record RegisterCashMovementCommand(
            Long cashSessionId,
            MovementType type,
            String concept,
            BigDecimal amount,
            String responsible
    ) {}
}
