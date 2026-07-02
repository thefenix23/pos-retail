package com.postretail.backend.sale.infrastructure.adapter;

import com.postretail.backend.cashsession.domain.port.out.CashSessionRepository;
import com.postretail.backend.sale.domain.port.out.CashSessionStatusPort;
import org.springframework.stereotype.Component;

/**
 * Punto único de contacto entre sale y cashsession.
 * Implementa el puerto que sale definió (CashSessionStatusPort) y por dentro
 * delega en el repositorio de cashsession.
 */
@Component
public class CashSessionStatusAdapter implements CashSessionStatusPort {

    private final CashSessionRepository cashSessionRepository;

    public CashSessionStatusAdapter(CashSessionRepository cashSessionRepository) {
        this.cashSessionRepository = cashSessionRepository;
    }

    @Override
    public Long findOpenSessionId(Long branchId, Long registerId) {
        return cashSessionRepository
                .findOpenSession(branchId, registerId)
                .map(session -> session.getId())
                .orElse(null);
    }
}
