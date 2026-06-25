package com.postretail.backend.sale.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Sale {

    private final Long id;
    private final List<SaleItem> items;
    private final Long paymentMethodId;
    private final BigDecimal total;
    private final String status;
    private final LocalDateTime createdAt;

    // Constructor para crear una venta nueva (sin id ni fecha aún)
    public Sale(Long id, List<SaleItem> items, Long paymentMethodId) {
        this(id, items, paymentMethodId, null);
    }

    // Constructor completo
    public Sale(Long id, List<SaleItem> items, Long paymentMethodId, LocalDateTime createdAt) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Una venta debe tener al menos un articulo");
        }

        if (paymentMethodId == null) {
            throw new IllegalArgumentException("Se requiere un método de pago");
        }

        this.id = id;
        this.items = List.copyOf(items);
        this.paymentMethodId = paymentMethodId;
        this.total = items
                .stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.status = "COMPLETED";
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
