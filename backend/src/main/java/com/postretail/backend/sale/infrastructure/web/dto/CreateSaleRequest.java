package com.postretail.backend.sale.infrastructure.web.dto;

import java.util.List;

public record CreateSaleRequest (
        List<SaleItemRequest> items,
        List<PaymentRequest> payments
) {}
