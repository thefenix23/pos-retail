package com.postretail.backend.cashsession.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Sessión de caja
 */
public class CashSession {

    private final Long id;
    private final Long branchId;
    private final Long registerId;
    private final String cashier;
    private final BigDecimal initialFund;
    private final CashStatus status;
    private final LocalDateTime openedAt;
    private final LocalDateTime closedAt;

    public CashSession(Long id, Long branchId, Long registerId, String cashier, BigDecimal initialFund, CashStatus status, LocalDateTime openedAt, LocalDateTime closedAt) {
        this.id = id;
        this.branchId = branchId;
        this.registerId = registerId;
        this.cashier = cashier;
        this.initialFund = initialFund;
        this.status = status;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
    }

    /**
     * Crea una nueva sesión ABIERTA aplicando las invariantes de apertura
     * El id, openedAt los asigna la persistencia (id) o el momento actual
     */
    public static CashSession open(Long branchId, Long registerId, String cashier, BigDecimal initialFund) {
        if (branchId == null || branchId <= 0) {
            throw new IllegalArgumentException("La sucursal es obligatoria");
        }

        if (registerId == null || registerId <= 0) {
            throw new IllegalArgumentException("La caja es obligatoria");
        }

        if (cashier == null || cashier.isBlank()) {
            throw new IllegalArgumentException("El cajero es obligatorio");
        }

        if (initialFund == null || initialFund.signum() < 0) {
            throw new IllegalArgumentException("El fondo inicial no puede ser negativo");
        }

        return new CashSession(
                null,
                branchId,
                registerId,
                cashier,
                initialFund,
                CashStatus.OPEN,
                LocalDateTime.now(),
                null
        );
    }

    /**
     * Devuelve una copia de esta sesión en estado CERRADO, con la fecha de cierre.
     * El dominio no muta: produce una nueva instancia cerrada.
     */
    public CashSession close() {
        return new CashSession(
                this.id,
                this.branchId,
                this.registerId,
                this.cashier,
                this.initialFund,
                CashStatus.CLOSED,
                this.openedAt,
                LocalDateTime.now()
        );
    }

    public boolean isOpen() {
        return status == CashStatus.OPEN;
    }

    public Long getId() {
        return id;
    }

    public Long getBranchId() {
        return branchId;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public String getCashier() {
        return cashier;
    }

    public BigDecimal getInitialFund() {
        return initialFund;
    }

    public CashStatus getStatus() {
        return status;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }
}
