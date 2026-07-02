package com.postretail.backend.cashsession.domain.port.out;

import com.postretail.backend.cashsession.domain.model.CashSession;

import java.util.Optional;

/**
 * Puerto de salida: lo que el dominio necesita del exterior para operar.
 * Lo implementa un adaptador @Component en infraestructura.
 */
public interface CashSessionRepository {
    /**
     * Indica si ya existe una sesión ABIERTA para esa caja física.
     * Permite el caso de uso aplicar la regla "Una sola caja abierta a la vez"
     */
    boolean existsOpenSession(Long branchId, Long registerId);

    /**
     * Devuelve la sessión ABIERTA de esa caja física, si existe.
     * La usa el módulo sale (vía adaptador) para asociar la venta a su caja
     */
    Optional<CashSession> findOpenSession(Long branchId, Long registerId);

    /**
     * Busca una sesión por su id. La usa el cierre para cargar la sesión a cerrar.
     */
    Optional<CashSession> findById(Long id);

    /**
     * Persiste la sesión y devuelve la versió con id asignado.
     */
    CashSession save(CashSession session);

    /**
     * Actualiza una sesión existente (por ejmplo, al cerrarla
     */
    CashSession update(CashSession session);
}
