package com.postretail.backend.sale.domain.port.out;

/**
 * Puerto de salida propiedad del módulo sale.
 * Le permite saber si hay una caja abierta sin conocer las clases del módulo cashsession.
 * Un adaptador en infraestructura lo implementa delegando cashsession.
 */
public interface CashSessionStatusPort {
    /**
     * Devuelve el id de la sesión de caja abierta para esa caja física,
     * o null si no hay ninguna abierta.
     */
    Long findOpenSessionId(Long branchId, Long registerId);
}
