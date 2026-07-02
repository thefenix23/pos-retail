package com.postretail.backend.sale.infrastructure.web.dto;

import com.postretail.backend.sale.domain.model.Sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// Detalle completo de una venta, con el nombre de cada producto para reimprimir el ticket
public record SaleDetailResponse(
        Long id,
        String status,
        BigDecimal total,
        BigDecimal paidAmount,
        BigDecimal change,
        Long cashSessionId,
        LocalDateTime createdAt,
        List<Payment> payments,
        List<Item> items
) {
    public record Item(
            Long productId,
            String productName,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {}

    public record Payment(
            Long paymentMethodId,
            BigDecimal amount
    ) {}

    // Recibe el mapa de nombres (productId -> nombre) para enriquecer cada linea
    public static SaleDetailResponse fromDomain(Sale sale, Map<Long, String> productNames) {
        List<Item> items = sale
                .getItems()
                .stream()
                .map(i -> new Item(
                        i.getProductId(),
                        productNames.getOrDefault(
                                i.getProductId(),
                                "Producto eliminado"
                        ),
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

        return new SaleDetailResponse(
                sale.getId(),
                sale.getStatus(),
                sale.getTotal(),
                sale.getPaidAmount(),
                sale.getChange(),
                sale.getCashSessionId(),
                sale.getCreatedAt(),
                payments,
                items
        );
    }
}
