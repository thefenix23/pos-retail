package com.postretail.backend.cashsession.domain.exception;

public class CashSessionNotOpenException extends RuntimeException {

    public CashSessionNotOpenException(Long cashSessionId) {
        super("La caja " + cashSessionId + " no está abierta");
    }

    public CashSessionNotOpenException(String message) {
        super(message);
    }
}
