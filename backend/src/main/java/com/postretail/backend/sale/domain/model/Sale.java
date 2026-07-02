package com.postretail.backend.sale.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Sale {

    private final Long id;
    private final List<SaleItem> items;
    private final List<SalePayment> payments;
    private final Long cashSessionId;
    private final BigDecimal total;
    private final BigDecimal paidAmount;
    private final BigDecimal change;
    private final String status;
    private final LocalDateTime createdAt;

    // Constructor para crear una venta nueva (sin id ni fecha aún)
    public Sale(Long id, List<SaleItem> items, List<SalePayment> payments, Long cashSessionId) {
        this(id, items, payments, cashSessionId, null);
    }

    // Constructor completo
    public Sale(Long id, List<SaleItem> items,List<SalePayment> payments, Long cashSessionId, LocalDateTime createdAt) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Una venta debe tener al menos un articulo");
        }

        if (payments == null || payments.isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos un pago");
        }

        if (cashSessionId == null) {
            throw new IllegalArgumentException("La venta debe pertenecer a una caja abierta");
        }

        this.id = id;
        this.items = List.copyOf(items);
        this.payments = List.copyOf(payments);
        this.cashSessionId = cashSessionId;
        this.total = items
                .stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.paidAmount = payments
                .stream()
                .map(SalePayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Regla de pagos mixtos: lo pagado debe cubrir el total.
        if (this.paidAmount.compareTo(this.total) < 0) {
            throw new IllegalArgumentException("Los pagos no cubren el total de la venta");
        }

        // El cambio es el excedente. Nunca negativo por la validación anterior.
        this.change = this.paidAmount.subtract(this.total);
        this.status = "COMPLETED";
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public List<SalePayment> getPayments() {
        return payments;
    }

    public Long getCashSessionId() {
        return cashSessionId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public BigDecimal getChange() {
        return change;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
