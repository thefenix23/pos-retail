package com.postretail.backend.sale.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class Sale {

    private final Long id;
    private final List<SaleItem> items;
    private final Long paymentMethodId;
    private final BigDecimal total;
    private final String status;

    public Sale(Long id, List<SaleItem> items, Long paymentMethodId) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("A sale must have at least one item");
        }

        if (paymentMethodId == null) {
            throw new IllegalArgumentException("Payment method is required");
        }

        this.id = id;
        this.items = List.copyOf(items);
        this.paymentMethodId = paymentMethodId;
        this.total = items
                .stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.status = "COMPLETED";
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
}
