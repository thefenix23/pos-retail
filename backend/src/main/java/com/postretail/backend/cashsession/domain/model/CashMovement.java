package com.postretail.backend.cashsession.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Movimiento de efectivo ajeno a una venta (retiro, deposito, gasto)
 */
public class CashMovement {

    private final Long id;
    private final Long cashSessionId;
    private final MovementType type;
    private final String concept;
    private final BigDecimal amount;
    private final String responsible;
    private final LocalDateTime createdAt;

    public CashMovement(Long id, Long cashSessionId, MovementType type, String concept, BigDecimal amount, String responsible, LocalDateTime createdAt) {
        this.id = id;
        this.cashSessionId = cashSessionId;
        this.type = type;
        this.concept = concept;
        this.amount = amount;
        this.responsible = responsible;
        this.createdAt = createdAt;
    }

    /**
     * Crea un movimiento nuevo aplicando sus invariantes.
     * El monto siempre es positivo; lo que cambia el efectivo es el tipo.
     */
    public static CashMovement register(Long cashSessionId, MovementType type, String concept, BigDecimal amount, String responsible) {
        if (cashSessionId == null) {
            throw new IllegalArgumentException("El movimiento debe pertenecer a una caja");
        }

        if (type == null) {
            throw new IllegalArgumentException("El tipo de movimiento es obligatorio");
        }

        if (concept == null || concept.isBlank()) {
            throw new IllegalArgumentException("El concepto es obligatorio");
        }

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        if (responsible == null || responsible.isBlank()) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }

        return new CashMovement(
                null,
                cashSessionId,
                type,
                concept,
                amount,
                responsible,
                LocalDateTime.now()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getCashSessionId() {
        return cashSessionId;
    }

    public MovementType getType() {
        return type;
    }

    public String getConcept() {
        return concept;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getResponsible() {
        return responsible;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
