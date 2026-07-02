package com.postretail.backend.sale.infrastructure.web.dto;

import com.postretail.backend.sale.domain.model.Sale;

import java.math.BigDecimal;
import java.util.List;

public record SaleResponse (
        Long id,
        String status,
        BigDecimal total,
        BigDecimal paidAmount,
        BigDecimal change,
        Long cashSessionId,
        List<Payment> payments,
        List<Item> items
) {
    public record Item(
            Long productId,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {}

    public record Payment(
            Long paymentMethodId,
            BigDecimal amount
    ) {}

    public static SaleResponse fromDomain(Sale sale) {
        List<Item> items = sale
                .getItems()
                .stream()
                .map(
                        i -> new Item(
                                i.getProductId(),
                                i.getQuantity(),
                                i.getUnitPrice(),
                                i.getSubtotal()
                        ))
                .toList();

        List<Payment> payments = sale
                .getPayments()
                .stream()
                .map(
                        p -> new Payment(
                                p.getPaymentMethodId(),
                                p.getAmount()
                        )
                )
                .toList();

        return new SaleResponse(
                sale.getId(),
                sale.getStatus(),
                sale.getTotal(),
                sale.getPaidAmount(),
                sale.getChange(),
                sale.getCashSessionId(),
                payments,
                items
        );
    }
}
