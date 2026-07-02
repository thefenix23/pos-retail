package com.postretail.backend.cashsession.domain.port.out;

import com.postretail.backend.cashsession.domain.model.CashCount;

import java.util.Optional;

/**
 * Puerto de salida para persistir y consultar el corte de caja.
 */
public interface CashCountRepository {

    CashCount save(CashCount count);

    /**
     * Corte de una sesión, si ya existe (cada sesión tiene a lo sumo uno)
     */
    Optional<CashCount> findBySessionId(Long cashSessionId);
}
