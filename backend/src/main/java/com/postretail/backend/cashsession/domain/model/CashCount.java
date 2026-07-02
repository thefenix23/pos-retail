package com.postretail.backend.cashsession.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Corte de caja: el arqueo que se genera al cerrar el turno.
 * Calcula el efectivo esperado y la diferencia contra lo contado
 */
public class CashCount {

    private final Long id;
    private final Long cashSessionId;
    private final BigDecimal expectedCash;
    private final BigDecimal countedCash;
    private final BigDecimal difference;
    private final String approvedBy;
    private final LocalDateTime createdAt;

    public CashCount(Long id, Long cashSessionId, BigDecimal expectedCash, BigDecimal countedCash, BigDecimal difference, String approvedBy, LocalDateTime createdAt) {
        this.id = id;
        this.cashSessionId = cashSessionId;
        this.expectedCash = expectedCash;
        this.countedCash = countedCash;
        this.difference = difference;
        this.approvedBy = approvedBy;
        this.createdAt = createdAt;
    }

    /**
     * Genera el corte calculado el efectivo esperado y la diferencia.
     *
     * Efectivo esperado = fondo inicial
     *                   + venta en efectivo
     *                   + ingresos (INCOME)
     *                   - egresos (EXPENSE)
     *
     * diferencia = contado - esperado (negativa = falta, positiva = sobra)
     */
    public static CashCount generate(Long cashSessionId, BigDecimal initialFund, BigDecimal cashSalesTotal, BigDecimal incomeTotal, BigDecimal expenseTotal, BigDecimal countedCash, String approvedBy) {
        if (cashSessionId == null) {
            throw new IllegalArgumentException("El corte debe pertenecer a una caja");
        }

        if (countedCash == null || countedCash.signum() < 0) {
            throw new IllegalArgumentException("El efectivo contado no puede ser negativo");
        }

        if (approvedBy == null || approvedBy.isBlank()) {
            throw new IllegalArgumentException("Se requiere quién aprueba el corte");
        }

        BigDecimal expected = initialFund
                .add(cashSalesTotal)
                .add(incomeTotal)
                .subtract(expenseTotal);

        BigDecimal difference = countedCash.subtract(expected);

        return new CashCount(
                null,
                cashSessionId,
                expected,
                countedCash,
                difference,
                approvedBy,
                LocalDateTime.now()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getCashSessionId() {
        return cashSessionId;
    }

    public BigDecimal getExpectedCash() {
        return expectedCash;
    }

    public BigDecimal getCountedCash() {
        return countedCash;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
