package com.postretail.backend.cashsession.application.usecase;

import com.postretail.backend.cashsession.domain.exception.CashSessionNotOpenException;
import com.postretail.backend.cashsession.domain.model.CashMovement;
import com.postretail.backend.cashsession.domain.model.CashSession;
import com.postretail.backend.cashsession.domain.port.in.RegisterCashMovementUseCase;
import com.postretail.backend.cashsession.domain.port.out.CashMovementRepository;
import com.postretail.backend.cashsession.domain.port.out.CashSessionRepository;

/**
 * Registra un ingreso o egreso de efectivo.
 * Regla: solo se puede registrar en una caja que está abierta.
 */
public class RegisterCashMovementService implements RegisterCashMovementUseCase {

    private final CashSessionRepository cashSessionRepository;
    private final CashMovementRepository cashMovementRepository;

    public RegisterCashMovementService(CashSessionRepository cashSessionRepository, CashMovementRepository cashMovementRepository) {
        this.cashSessionRepository = cashSessionRepository;
        this.cashMovementRepository = cashMovementRepository;
    }

    @Override
    public CashMovement register(RegisterCashMovementCommand command) {
        // La caja debe existir y estar abierta para aceptar movimientos.
        CashSession session = cashSessionRepository
                .findById(command.cashSessionId())
                .orElseThrow(
                        () -> new CashSessionNotOpenException(
                                command.cashSessionId()
                        )
                );

        if (!session.isOpen()) {
            throw new CashSessionNotOpenException(command.cashSessionId());
        }

        // El dominio valida sus invariantes (concepto, monto positivo, responsable).
        CashMovement movement = CashMovement.register(
                command.cashSessionId(),
                command.type(),
                command.concept(),
                command.amount(),
                command.responsible()
        );

        return cashMovementRepository.save(movement);
    }
}
