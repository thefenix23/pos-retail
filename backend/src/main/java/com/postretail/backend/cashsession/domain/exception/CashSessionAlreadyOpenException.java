package com.postretail.backend.cashsession.domain.exception;

/**
 * Se lanza al intentar abrir una caja que ya tiene una sesión abierta.
 */
public class CashSessionAlreadyOpenException extends RuntimeException {

    public CashSessionAlreadyOpenException(Long branchId, Long registerId) {
        super("Ya existe una caja abierta para la sucursal " + branchId
        + "y caja " + registerId);
    }
}
