package com.postretail.backend.cashsession.application.usecase;

import com.postretail.backend.cashsession.domain.exception.CashSessionNotOpenException;
import com.postretail.backend.cashsession.domain.model.CashCount;
import com.postretail.backend.cashsession.domain.model.CashSession;
import com.postretail.backend.cashsession.domain.model.MovementType;
import com.postretail.backend.cashsession.domain.port.in.CloseCashSessionUseCase;
import com.postretail.backend.cashsession.domain.port.out.CashCountRepository;
import com.postretail.backend.cashsession.domain.port.out.CashMovementRepository;
import com.postretail.backend.cashsession.domain.port.out.CashSalesSummaryPort;
import com.postretail.backend.cashsession.domain.port.out.CashSessionRepository;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

/**
 * Cierra una caja abierta y genera su corte (arqueo)
 * Orquesta los puertos para reunir los totales y delega el cálculo al dominio.
 */
public class CloseCashSessionService implements CloseCashSessionUseCase {

    private final CashSessionRepository cashSessionRepository;
    private final CashMovementRepository cashMovementRepository;
    private final CashCountRepository cashCountRepository;
    private final CashSalesSummaryPort cashSalesSummaryPort;

    public CloseCashSessionService(CashSessionRepository cashSessionRepository, CashMovementRepository cashMovementRepository, CashCountRepository cashCountRepository, CashSalesSummaryPort cashSalesSummaryPort) {
        this.cashSessionRepository = cashSessionRepository;
        this.cashMovementRepository = cashMovementRepository;
        this.cashCountRepository = cashCountRepository;
        this.cashSalesSummaryPort = cashSalesSummaryPort;
    }

    @Override
    @Transactional
    public CashCount close(CloseCashSessionCommand command) {
        Long sessionId = command.cashSessionId();

        // 1. La caja debe existir y estar abierta para poder cerrarse.
        CashSession session = cashSessionRepository
                .findById(sessionId)
                .orElseThrow(
                        () -> new CashSessionNotOpenException(sessionId)
                );

        if (!session.isOpen()) {
            throw new CashSessionNotOpenException(
                    "La caja " + sessionId + " ya está cerrada o no puede cerrarse"
            );
        }

        // 2. Reunir los totales que alimentan el efectivo esperado.
        BigDecimal cashSales = cashSalesSummaryPort.cashTotalForSession(sessionId);
        BigDecimal incomes = cashMovementRepository.totalByType(
                sessionId,
                MovementType.INCOME.name()
        );
        BigDecimal expenses = cashMovementRepository.totalByType(
                sessionId,
                MovementType.EXPENSE.name()
        );

        // 3. El dominio calcula el esperado y la diferencia contra lo contado.
        CashCount count = CashCount.generate(
                sessionId,
                session.getInitialFund(),
                cashSales,
                incomes,
                expenses,
                command.countedCash(),
                command.approvedBy()
        );

        // 4. Persistir el corte y cerrar la sesión.
        CashCount savedCount = cashCountRepository.save(count);
        cashSessionRepository.update(session.close());

        return savedCount;
    }
}
