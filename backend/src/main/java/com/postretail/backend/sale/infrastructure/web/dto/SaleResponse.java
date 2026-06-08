package com.postretail.backend.sale.infrastructure.web.dto;

import com.postretail.backend.sale.domain.model.Sale;

import java.math.BigDecimal;
import java.util.List;

public record SaleResponse (
        Long id,
        String status,
        BigDecimal total,
        Long paymentMethodId,
        List<Item> items
) {
    public record Item(
            Long productId,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
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

        return new SaleResponse(
                sale.getId(),
                sale.getStatus(),
                sale.getTotal(),
                sale.getPaymentMethodId(),
                items
        );
    }
}
