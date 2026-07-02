package com.postretail.backend.sale.infrastructure.web.dto;

import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.model.SaleItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Versión ligera para la lista de ventas (sin el detalle de items)
public record SaleSummaryResponse(
        Long id,
        String status,
        BigDecimal total,
        int itemCount,
        int paymentCount,
        LocalDateTime createdAt
) {
    public static SaleSummaryResponse fromDomain(Sale sale) {
        int count = sale
                .getItems()
                .stream()
                .mapToInt(SaleItem::getQuantity)
                .sum();

        return new SaleSummaryResponse(
                sale.getId(),
                sale.getStatus(),
                sale.getTotal(),
                count,
                sale.getPayments().size(),
                sale.getCreatedAt()
        );
    }
}
