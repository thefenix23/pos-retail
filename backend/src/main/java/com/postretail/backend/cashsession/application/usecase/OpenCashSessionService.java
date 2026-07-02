package com.postretail.backend.cashsession.application.usecase;

import com.postretail.backend.cashsession.domain.exception.CashSessionAlreadyOpenException;
import com.postretail.backend.cashsession.domain.model.CashSession;
import com.postretail.backend.cashsession.domain.port.in.OpenCashSessionUseCase;
import com.postretail.backend.cashsession.domain.port.out.CashSessionRepository;

import java.math.BigDecimal;

/**
 * Orquesta la apertura de caja.
 * Depende ÚNICAMENTE del puerto de salida (la interfaz), no de la persistencia.
 * No lleva anotaciones de Spring: se registra como @Bean con new en el Config.
 */
public class OpenCashSessionService implements OpenCashSessionUseCase {

    private final CashSessionRepository repository;

    public OpenCashSessionService(CashSessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public CashSession open(Long branchId, Long registerId, String cashier, BigDecimal initialFund) {
        // Regla de negocio: no se puede abrir una caja que ya está abierta.
        if (repository.existsOpenSession(branchId, registerId)) {
            throw new CashSessionAlreadyOpenException(branchId, registerId);
        }

        // El dominio valida sus propias invariantes (fondo no negativo, campos obligatorios).
        CashSession session = CashSession.open(branchId, registerId, cashier, initialFund);

        return repository.save(session);
    }
}
